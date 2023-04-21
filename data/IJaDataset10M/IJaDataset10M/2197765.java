
/*
 *  PHEX - The pure-java Gnutella-servent.
 *  Copyright (C) 2001 Konrad Haenel (www.konrad-haenel.de)
 *                     Gregor Koukkoullis ( phex@kouk.de )
 *  Copyright (C) 2000 William W. Wong
 *  williamw@jps.net
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package phex.download;

import java.io.*;
import java.net.*;
import java.util.*;
import org.w3c.dom.Document;
import org.apache.crimson.tree.XmlDocument;
import phex.*;
import phex.config.*;
import phex.connection.*;
import phex.common.TransferRateService;
import phex.download.*;
import phex.host.*;
import phex.interfaces.*;
import phex.msg.*;
import phex.utils.*;
import phex.event.*;
import phex.xml.XMLDownloadList;
import phex.xml.XMLDownloadFile;
import phex.xml.XMLPhex;
import phex.xml.XMLPhexXMLCodec;
import phex.xml.XMLBuilder;

public class DownloadManager
{
    private static final int MAX_RETRIES = 500;

    private Vector					mDownloadList = new Vector();
    private Hashtable				mPendingPushTransfer = new Hashtable();
    private int						mWorkerCount = 0;
    private boolean				mShutdownAllWorkers = false;
    private int						mDownloadingCount = 0;
    private DataChanger			mStatusChangedListener = new DataChanger();
    private static boolean          lockDownloadFileList= false;
    private TransferRateService transferRateService;

    /**
     * All listeners interested in events.
     */
    private ArrayList listenerList = new ArrayList( 2 );

    public DownloadManager()
    {
        transferRateService = ServiceManager.getTransferRateService();
    }

    public void addStatusChangedListener(IDataChangedListener listener)
    {
        mStatusChangedListener.addListener(listener);
    }


    /*public void downloadChanged()
    {
        if (mDownloadChangedListener != null)
            mDownloadChangedListener.downloadChanged();

        mStatusChangedListener.dataChanged(null);
    }

    public void candidateChanged()
    {
        if (mCandidateListener != null)
            mCandidateListener.candidatesHaveChanged();
    }*/

    // Check to see if there are enough workers to perform the maximum allowed
    // number of concurrent downloads. If not enough, create them.
    public synchronized void createEnoughWorkers()
    {
        // One worker per one concurrent download.
        // Create up to the max concurrent downloads allowed.
        while( mWorkerCount < ServiceManager.sCfg.mMaxDownload )
        {
            // Increment the number of workers we have _before_ we create as they may use this count.
            ++mWorkerCount;

            // Create the worker
            new DownloadWorker( this );
        }
    }


    synchronized boolean isTooManyWorker()
    {
        if (mWorkerCount > ServiceManager.sCfg.mMaxDownload)
        {
            // The DownloadWorker who calls this must exit now.
            mWorkerCount--;
            return true;
        }
        return false;
    }


    private boolean tooManyDownloadPerIP(DownloadFile download1)
    {
        int     count = 0;
        try
        {
            RemoteFile remoteFile = download1.getCurrentRemoteFile();
            if ( remoteFile == null )
            {
                return false;
            }

            HostAddress hostAddress = remoteFile.getQueryHitHost().getHostAddress();
            for (int i = 0; i < mDownloadList.size(); i++)
            {
                DownloadFile download;
                // we don't want to lock mDownloadList to long.
                synchronized( mDownloadList )
                {
                    if (i < mDownloadList.size())
                    {
                        download = (DownloadFile)mDownloadList.elementAt(i);
                    }
                    else
                    {
                        continue;
                    }
                }
                RemoteFile file = download.getCurrentRemoteFile();
                if (download == download1 || file == null
                    || file.getQueryHitHost().getHostAddress() == null)
                {
                    continue;
                }

                if (file.getQueryHitHost().getHostAddress().equals( hostAddress ) &&
                    (download.getStatus() == DownloadFile.sDownloading ||
                     download.getStatus() == DownloadFile.sConnecting ||
                     download.getStatus() == DownloadFile.sRequestPushTransfer))
                 {
                     count++;
                 }
            }
        }
        catch (Exception e)
        {
            Logger.logError( e, "Ignore out of range error in tooManyDownloadPerIP.");
            return true;
        }
        return (count >= ServiceManager.sCfg.mMaxDownloadPerIP);
    }


    public synchronized boolean addFileToDownload(RemoteFile rfile)
    {
        String strSearchTerm = StrUtil.createNaturalSearchTerm( rfile.getShortname() );
        return addFileToDownload( rfile, strSearchTerm, rfile.getFilename() );
    }

    /**
     * Checks if a new local filename for the given download file is already
     * used in any other download file ( except the given one of course )
     * If no download file is given the filename is checked against all download
     * files.
     */
    public boolean isNewLocalFilenameUsed( DownloadFile currentDownloadFile,
        String newLocalFilename )
    {
        // Check for duplicate filename in the existing files to download.
        for (int i = 0; i < mDownloadList.size(); i++)
        {
            DownloadFile existingFile = (DownloadFile)mDownloadList.elementAt(i);

            // check file name if currentDownloadFile is null or existingFile is
            // not the currentDownloadFile
            if ( currentDownloadFile == null || !(existingFile == currentDownloadFile) )
            {
                if ( existingFile.getDestinationFileName().equals( newLocalFilename ) )
                {
                    // filename is already used
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized boolean addFileToDownload(RemoteFile rfile,
        String searchTerm, String localFilename )
    {
        // Check for duplicate filename in the existing files to download.
        for (int i = 0; i < mDownloadList.size(); i++)
        {
            DownloadFile existingFile = (DownloadFile)mDownloadList.elementAt(i);

            if (existingFile.getDestinationFileName().equals( localFilename ) )
            {
                if (existingFile.getTotalDataSize() == rfile.getFileSize())
                {
                    existingFile.addRemoteCandidate( rfile );
                    try
                    {
                        ServiceManager.getSwarmingManager().saveDownloadList();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    notifyAll();
                    return true;
                }

                // Duplicate filename being downloaded.
                return false;
            }
        }

        // Make sure there are enough workers to do download.
        createEnoughWorkers();

        DownloadFile download = new DownloadFile( rfile, searchTerm, localFilename );
        int pos = mDownloadList.size();
        mDownloadList.insertElementAt( download, pos );
        fireDownloadFileAdded( pos );
        download.setStatus( DownloadFile.sQueued );
        try
        {
            ServiceManager.getSwarmingManager().saveDownloadList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        notifyAll();
        return true;
    }


    public synchronized DownloadFile getNextFileToDownload(DownloadWorker worker)
    {
        while (true)
        {
            if (mShutdownAllWorkers)
                return null;

            // resolve: verify that notifyall when number of max workers changed.
            if (isTooManyWorker())
            {
                // Too many workers to do download. I should exit the thread.
                // Put the file back into queuing mode.
                return null;
            }

            for (int i = 0; i < mDownloadList.size(); i++)
            {
                DownloadFile	download = (DownloadFile)mDownloadList.elementAt(i);

                if ( download.getStatus() == DownloadFile.sQueued )
                {
                    if ( download.getCandidateCount() == 0 )
                    {// if no download candidates found
                        download.setStatus( DownloadFile.sNoCandidates );
                        continue;
                    }
                    if (tooManyDownloadPerIP(download))
                    {
                        continue;
                    }
                    download.setStatus(DownloadFile.sConnecting);
                    download.setDownloadWorker(worker);

                    return download;
                }
            }

            // No download waiting to be processed.  Go to sleep.
            try
            {
                wait();
            }
            catch ( InterruptedException e)
            {
            }
        }
    }

    public XMLDownloadList createXMLDownloadList()
    {
        XMLDownloadList list = new XMLDownloadList();
        Enumeration enum = mDownloadList.elements();
        while ( enum.hasMoreElements() )
        {
            DownloadFile file = (DownloadFile) enum.nextElement();
            list.addXMLDownloadFile( file.createXMLDownloadFile() );
        }
        return list;
    }

    public void loadXMLDownloadInfo( XMLDownloadList list )
    {
        mDownloadList.clear();
        Iterator iterator = list.createXMLDownloadFileIterator();
        while ( iterator.hasNext() )
        {
            XMLDownloadFile xfile = (XMLDownloadFile) iterator.next();
            DownloadFile dfile = new DownloadFile( xfile );
            int pos = mDownloadList.size();
            mDownloadList.insertElementAt( dfile, pos );
            fireDownloadFileAdded( pos );
        }
        createEnoughWorkers();
    }

    public synchronized void incDownloadingCount()
    {
        mDownloadingCount++;
    }


    public synchronized void decDownloadingCount()
    {
        mDownloadingCount--;
    }


    public int getDownloadingCount()
    {
        return mDownloadingCount;
    }

    /**
     * @deprecated
     */
    public synchronized void stopDownload(int index)
    {
        if (index >= mDownloadList.size())
        {
            return;
        }
        DownloadFile download = (DownloadFile)mDownloadList.elementAt(index);
        stopDownload( download );
    }

    public synchronized void stopDownload( DownloadFile download )
    {
        if ( download.isDownloadInProgress() )
        {
            // Download in progress.  Tell the worker to stop it.
            download.setStatus(DownloadFile.sStopping);
            download.setStoppingTime( System.currentTimeMillis() );
            download.getDownloadWorker().stopDownload();
        }
        else
        {
            // Just change the status to sStopped.
            download.setStatus(DownloadFile.sStopped);
        }
    }

    /**
     * @deprecated
     */
    public synchronized void resumeDownload(int index)
    {
        if (index >= mDownloadList.size())
            return;

        DownloadFile download = (DownloadFile)mDownloadList.elementAt(index);
        resumeDownload( download );
    }

    public synchronized void resumeDownload( DownloadFile download )
    {
        if (download.getStatus() == DownloadFile.sStopped ||
            download.getStatus() == DownloadFile.sError)
        {
            if ( download.getCandidateCount() == 0 )
            {
                download.setStatus(DownloadFile.sNoCandidates);
            }
            else
            {
                download.setStatus(DownloadFile.sQueued);
            }
            download.setRetryCount(1);
            notifyAll();
        }
    }


    public synchronized void removeDownload(int index)
    {
        if (index >= mDownloadList.size())
        {
            return;
        }

        DownloadFile download = (DownloadFile)mDownloadList.elementAt(index);
        mDownloadList.removeElementAt(index);
        transferRateService.unregisterTransferDataProvider( download );
        fireDownloadFileRemoved( index );

        DownloadWorker	worker = download.getDownloadWorker();
        if (worker != null)
        {
            // Download in progress.  Tell the worker to stop it and remove file.
            download.setStatus(DownloadFile.sRemoved);
            worker.stopDownload();
        }
        else
        {
            // Remove intermediate file.
            File f = new File(download.getDownloadName());
            if (f.exists())
            {
                f.delete();
            }
        }

        try
        {
            ServiceManager.getSwarmingManager().saveDownloadList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public synchronized void removeCompleted()
    {
        for (int i = 0; i < mDownloadList.size(); )
        {
            DownloadFile download = (DownloadFile)mDownloadList.elementAt(i);

            if (download.getStatus() == DownloadFile.sCompleted)
            {
                mDownloadList.removeElementAt(i);
                transferRateService.unregisterTransferDataProvider( download );
                fireDownloadFileRemoved( i );
            }
            else
            {
                i++;
            }
        }
    }

    public DownloadFile getDownloadFileAt( int index )
    {
        synchronized( mDownloadList )
        {
            if ( index < 0 || index >= mDownloadList.size() )
            {
                return null;
            }
            return (DownloadFile) mDownloadList.get( index );
        }
    }

    public int getDownloadCount()
    {
        return mDownloadList.size();
    }

    /**
    * Called by ReadWorker to read a file being pushed from the remote host.
    * The pushing host is entered into the mPendingPushTransfer-Hashmap instead
    * of "none".
    **/
    public synchronized boolean notifyReadPushedFile(ConnectionRequest req, Host remoteHost)
    {
        // GIV <file-ref-num>:<ClientID GUID in hexdec>
        String key = req.getHeader("FileIndex") + ":" + req.getHeader("ClientGUID");

        if (mPendingPushTransfer.get(key) != null)
        {
            // Transfer remoteHost and its connection to the download thread.
            mPendingPushTransfer.put(key, remoteHost);

            // Wake up the download thread.
            notifyAll();
            return true;
        }
        return false;
    }


    /**
     * Called by DownloadWorker to send out a PushRequest and wait for incoming push connection.
     */
    public synchronized Host pushRequestTransfer( Host oldHost, DownloadFile download )
            throws Exception
    {
        download.setStatus( DownloadFile.sRequestPushTransfer );

        RemoteFile file = download.getCurrentRemoteFile();

        // Prepare the PushRequest msg.
        MsgPushRequest	msg = new MsgPushRequest( file.getRemoteClientID(),
            file.getFileIndex(), ServiceManager.getListener().getLocalAddress() );

        // Save it in pending transfer table using greeting as the key.
        // GIV <file-ref-num>:<ClientID GUID in hexdec>
        String key = msg.getFileIndex() + ":" + msg.getClientGUID().toHexString();
        mPendingPushTransfer.put(key, "none");

        download.appendLog(key);

        // Route the PushRequest msg.
        Host returnHost = ServiceManager.getMsgManager().getPushRouting(msg.getClientGUID());

        // it simply won´t work, so the candidate can be kicked
        if (returnHost == null)
        {
            download.removeCurrentCandidate();
            throw new IOException("Routing table has no info to route back to sharing host.");
        }
        ServiceManager.getSendManager().queueMsgToSend(returnHost, msg, false);

        download.appendLog("Wait for connection from sharing host");

        // Sleep and wait for the incoming connection from the pushing host.
        long then = System.currentTimeMillis();

        while (true)
        {
            if (mPendingPushTransfer.get(key) != "none")
            {
                break;
            }

            long timeElapsed = System.currentTimeMillis() - then;
            long timeToSleep = ServiceManager.sCfg.mPushTransferTimeout - timeElapsed;

            if (timeToSleep < 0)
            {
                // Timeout.
                mPendingPushTransfer.remove(key);
                download.appendLog("Time out waiting for connection from sharing host");
                return null;
            }

            try
            {
                wait(timeToSleep + 1000);
            }
            catch (Exception e)
            {
                // ignore
            }
        }

        download.appendLog("Sharing host made connection to me");
        return (Host)mPendingPushTransfer.remove(key);
    }


    /**
     * Called by JobsRunner periodically to retry failed download.
     **/
    public void retryDownloads()
    {
        int		changedCount = 0;

        for (int i = 0; i < mDownloadList.size(); i++)
        {
            DownloadFile download = (DownloadFile)mDownloadList.elementAt(i);
            int intDownloadStatus = download.getStatus();

            if (intDownloadStatus == DownloadFile.sError
                || intDownloadStatus == DownloadFile.HOST_BUSY )
            {
                if (download.isErrorStatusExpired())
                {
                    changedCount++;

                    /* why do we have to do something???
                    // after MAX_RETRIES download attempts we have to do something
                    // - set the count to zero again (must be done better)
                    // - search for new candidates
                    if ( download.getRetryCount() > 0 &&
                        (download.getRetryCount() % MAX_RETRIES == 0) &&
                        (download.getRetryCount() != download.getRetryLast()))
                    {
                        download.searchCandidate();
                        //System.gc();
                        download.setRetryLast(download.getRetryCount());
                        continue;
                    }
                    */

                    if (download.advanceCurrCandidate())
                    {
                        // Have re-tried on all candidates.
                        // Sleep to wait for next retry.
                        download.setStatus(DownloadFile.sRetryWait);
                        download.setNextRetryTime(System.currentTimeMillis() + ServiceManager.sCfg.mDownloadRetryWait);
                    }
                    else
                    {
                        // Try next candidate.
                        download.setRetryCount(download.getRetryCount() + 1);
                        download.setStatus(DownloadFile.sQueued);
                    }
                }
            }
            // we check whether the file must wait longer for retry
            else if (intDownloadStatus == DownloadFile.sRetryWait)
            {
                if (System.currentTimeMillis() > download.getNextRetryTime())
                {
                    changedCount++;
                    download.setRetryCount(download.getRetryCount() + 1);
                    download.setStatus(DownloadFile.sQueued);
                }
            }
            // check if we can set a "no candidates" download to "queued"
            else if ( intDownloadStatus == DownloadFile.sNoCandidates )
            {
                if ( download.getCandidateCount() > 0 )
                {
                    changedCount++;
                    download.setStatus(DownloadFile.sQueued);
                    download.advanceCurrCandidate();
                }
            }
        }

        if (changedCount > 0)
        {
            synchronized (this)
            {
                notifyAll();
            }
        }
    }

    ///////////////////// START event handling methods ////////////////////////
    public void addDownloadFilesChangeListener( DownloadFilesChangeListener listener )
    {
        listenerList.add( listener );
    }

    public void removeDownloadFilesChangeListener( DownloadFilesChangeListener listener )
    {
        listenerList.remove( listener );
    }

    private void fireDownloadFileChanged( final int position )
    {
        // invoke update in event dispatcher
        AsynchronousDispatcher.invokeLater(
        new Runnable()
        {
            public void run()
            {
                Object[] listeners = listenerList.toArray();
                DownloadFilesChangeListener listener;
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for ( int i = listeners.length - 1; i >= 0; i-- )
                {
                    listener = (DownloadFilesChangeListener)listeners[ i ];
                    listener.downloadFileChanged( position );
                }
            }
        });
    }

    private void fireDownloadFileAdded( final int position )
    {
        // invoke update in event dispatcher
        AsynchronousDispatcher.invokeLater(
        new Runnable()
        {
            public void run()
            {
                Object[] listeners = listenerList.toArray();
                DownloadFilesChangeListener listener;
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for ( int i = listeners.length - 1; i >= 0; i-- )
                {
                    listener = (DownloadFilesChangeListener)listeners[ i ];
                    listener.downloadFileAdded( position );
                }
            }
        });
    }

    private void fireDownloadFileRemoved( final int position )
    {
        // invoke update in event dispatcher
        AsynchronousDispatcher.invokeLater(
        new Runnable()
        {
            public void run()
            {
                Object[] listeners = listenerList.toArray();
                DownloadFilesChangeListener listener;
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for ( int i = listeners.length - 1; i >= 0; i-- )
                {
                    listener = (DownloadFilesChangeListener)listeners[ i ];
                    listener.downloadFileRemoved( position );
                }
            }
        });
    }

    public void fireDownloadFileChanged( DownloadFile file )
    {
        int position = mDownloadList.indexOf( file );
        if ( position >= 0 )
        {
            fireDownloadFileChanged( position );
        }
    }
    ///////////////////// END event handling methods ////////////////////////

}
