package org.exos.tasks.exchange;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import net.sf.microlog.Logger;
import org.exos.ExosTaskErrorEvent;
import org.exos.ExosTaskResultEvent;
import org.exos.IExosTaskEventListener;
import org.exos.InternalErrorException;
import org.exos.crypto.CryptoFactory;
import org.exos.crypto.IAsymmetricCipher;
import org.exos.crypto.IBlockCipher;
import org.exos.crypto.ISigner;
import org.exos.crypto.InvalidDataException;
import org.exos.crypto.InvalidKeyException;
import org.exos.crypto.NotSupportedException;
import org.exos.dao.Account;
import org.exos.dao.DAL;
import org.exos.dao.DataStorageException;
import org.exos.dao.Fragment;
import org.exos.dao.Group;
import org.exos.exchange.DataAdapter;
import org.exos.exchange.HostNotAvailableException;
import org.exos.exchange.NoSuchProtocolException;
import org.exos.exchange.NotVerifiedException;
import org.exos.log.Log;
import org.exos.tasks.Task;
import org.exos.util.CryptoUtils;
import org.exos.util.TaskUtils;

public class SetFragmentTask extends Task {

    private DAL dal;

    private Logger log;

    private IExosTaskEventListener listener;

    private DataAdapter dada;

    private Account account;

    private Group group;

    private byte[] fragmentData;

    private String fragmentName;

    private boolean addFragment;

    private boolean deleteFragment = false;

    /**
	 * Delete fragment constructor.
	 */
    public SetFragmentTask(IExosTaskEventListener listener, int accountId, String fragmentId) throws InternalErrorException {
        log = Logger.getLogger();
        Log.configureLogger(log);
        this.listener = listener;
        addFragment = false;
        deleteFragment = true;
        fragmentName = fragmentId;
        try {
            dal = DAL.getInstance();
            account = dal.getAccount(accountId);
            dada = DataAdapter.loadAdapter(account.getHost().getHostProtocol());
            dada.initAdapter(account.getHost(), account.getTel(), account.getKpSignature().getPrivateKey());
        } catch (NoSuchProtocolException ex) {
            log.error(ex);
            throw new InternalErrorException(ex);
        } catch (Exception ex) {
            log.fatal(ex);
            throw new InternalErrorException(ex);
        }
    }

    /**
	 * Update fragment constructor.
	 */
    public SetFragmentTask(IExosTaskEventListener listener, int accountId, String fragmentId, byte[] data, int dataLen) throws InternalErrorException {
        log = Logger.getLogger();
        Log.configureLogger(log);
        this.listener = listener;
        fragmentData = new byte[dataLen];
        System.arraycopy(data, 0, fragmentData, 0, dataLen);
        addFragment = false;
        fragmentName = fragmentId;
        try {
            dal = DAL.getInstance();
            account = dal.getAccount(accountId);
            dada = DataAdapter.loadAdapter(account.getHost().getHostProtocol());
            dada.initAdapter(account.getHost(), account.getTel(), account.getKpSignature().getPrivateKey());
        } catch (NoSuchProtocolException ex) {
            log.error(ex);
            throw new InternalErrorException(ex);
        } catch (Exception ex) {
            log.fatal(ex);
            throw new InternalErrorException(ex);
        }
    }

    /**
	 * Add fragment constructor.
	 */
    public SetFragmentTask(IExosTaskEventListener listener, int accountId, String fragmentId, byte[] data, int dataLen, int groupId) throws InternalErrorException {
        log = Logger.getLogger();
        Log.configureLogger(log);
        this.listener = listener;
        fragmentData = new byte[dataLen];
        System.arraycopy(data, 0, fragmentData, 0, dataLen);
        addFragment = true;
        fragmentName = fragmentId;
        try {
            dal = DAL.getInstance();
            account = dal.getAccount(accountId);
            group = dal.getGroup(groupId);
            dada = DataAdapter.loadAdapter(account.getHost().getHostProtocol());
            dada.initAdapter(account.getHost(), account.getTel(), account.getKpSignature().getPrivateKey());
        } catch (NoSuchProtocolException ex) {
            log.error(ex);
            throw new InternalErrorException(ex);
        } catch (Exception ex) {
            log.fatal(ex);
            throw new InternalErrorException(ex);
        }
    }

    protected void doStuff() {
        try {
            dada.startUpdate();
            String fragmentUrl = null;
            Vector fragments = dal.getFragmentList(account.getId());
            IBlockCipher bc = CryptoFactory.getBlockCipher(account.getHost().getCrypto().getSymAlgorithm(), account.getHost().getCrypto().getSymFormat());
            ISigner si = CryptoFactory.getSigner(account.getHost().getCrypto().getSigAlgorithm(), account.getHost().getCrypto().getSigMDAlgorithm());
            Fragment fragment = null;
            if (deleteFragment) {
                Enumeration e = fragments.elements();
                while (e.hasMoreElements()) {
                    Fragment f = (Fragment) e.nextElement();
                    if (f.getFragmentName().equals(fragmentName)) {
                        dada.deleteData(fragmentName, f.getGroupId());
                        dal.deleteFragment(f.getId());
                        break;
                    }
                }
            } else if (addFragment) {
                if (group.getPeopleIds().size() == 0) {
                    throw new EmptyGroupException();
                }
                Enumeration e = fragments.elements();
                while (e.hasMoreElements()) {
                    Fragment f = (Fragment) e.nextElement();
                    if (f.getFragmentName().equals(fragmentName)) {
                        throw new DuplicateFragmentIdException(fragmentName);
                    }
                }
                fragment = new Fragment();
                fragment.setData(fragmentData);
                fragment.setFragmentLastModified(new Date().getTime());
                fragment.setFragmentName(fragmentName);
                fragment.setGroupId(group.getId());
                fragment.setMyFragment(true);
                dal.setFragment(fragment);
                fragmentUrl = TaskUtils.generateExosURL(account.getTel(), fragmentName, group.getId());
                byte[] cData = CryptoUtils.encryptFragmentData(account.getKpSignature().getPrivateKey(), group.getKey(), fragmentData, si, bc);
                dada.setData(fragmentName, fragment.getFragmentLastModified(), fragment.getGroupId(), cData, cData.length);
            } else {
                Enumeration e = fragments.elements();
                while (e.hasMoreElements()) {
                    Fragment f = (Fragment) e.nextElement();
                    if (f.getFragmentName().equals(fragmentName)) {
                        f.setData(fragmentData);
                        f.setFragmentLastModified(new Date().getTime());
                        dal.setFragment(f);
                        fragment = f;
                        break;
                    }
                }
                if (fragment == null) {
                    throw new InternalErrorException();
                }
                Group g = dal.getGroup(fragment.getGroupId());
                byte[] cData = CryptoUtils.encryptFragmentData(account.getKpSignature().getPrivateKey(), g.getKey(), fragmentData, si, bc);
                dada.setData(fragmentName, fragment.getFragmentLastModified(), fragment.getGroupId(), cData, cData.length);
            }
            listener.taskFinished(new ExosTaskResultEvent(taskId, fragmentUrl));
        } catch (NotSupportedException ex) {
            listener.taskAborted(new ExosTaskErrorEvent(taskId, new InternalErrorException(ex)));
        } catch (InternalErrorException ex) {
            listener.taskAborted(new ExosTaskErrorEvent(taskId, ex));
        } catch (HostNotAvailableException ex) {
            listener.taskAborted(new ExosTaskErrorEvent(taskId, ex));
        } catch (DuplicateFragmentIdException ex) {
            listener.taskAborted(new ExosTaskErrorEvent(taskId, ex));
        } catch (EmptyGroupException ex) {
            listener.taskAborted(new ExosTaskErrorEvent(taskId, ex));
        } catch (DataStorageException ex) {
            listener.taskAborted(new ExosTaskErrorEvent(taskId, new InternalErrorException(ex)));
        } finally {
            dada.finishUpdate();
        }
    }
}
