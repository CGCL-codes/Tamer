package org.fxplayer.service.folder;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.fxplayer.domain.Cover;
import org.fxplayer.domain.FolderLog;
import org.fxplayer.domain.Track;
import org.fxplayer.util.AudioFile;
import org.fxplayer.util.FileExtensionFilter;
import org.fxplayer.util.FileUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class UpdateJob.
 */
public class UpdateJob extends Job {

    /**
	 * The Class UpdateFileFilter.
	 * @author Sven Duzont
	 */
    private static final class UpdateFileFilter implements FileFilter {

        /** The tracks. */
        private final Map<String, Track> tracks;

        /**
		 * The Constructor.
		 * @param tracks
		 *          the tracks
		 */
        private UpdateFileFilter(final Map<String, Track> tracks) {
            this.tracks = tracks;
        }

        public boolean accept(final File file) {
            for (final String ext : extensions) if (file.getName().toLowerCase().endsWith(ext)) {
                final Track track = tracks.get(file.getPath());
                tracks.remove(file.getPath());
                if (track == null) return true;
                return track.getLastModified().getTime() < file.lastModified();
            }
            return false;
        }
    }

    /** The extensions. */
    private static final String[] extensions = { ".mp3" };

    /** The files. */
    private File[] files;

    /** The to delete. */
    protected final Collection<Integer> toDelete = new HashSet<Integer>();

    /**
	 * Instantiates a new update job.
	 */
    public UpdateJob() {
    }

    @Override
    protected void executeJob() {
        startScan();
        scanFiles();
        finalizeScan();
    }

    /**
	 * Finalize scan.
	 */
    @Transactional
    private void finalizeScan() {
        LOG.info("Finalizing scan");
        log.setStatus(FolderLog.Status.DELETING);
        FOLDER_LOG_DAO.merge(log);
        for (final Integer trackId : toDelete) {
            log.currentDeleted += 1;
            TRACK_DAO.deleteTrackById(trackId);
            if (LOG.isTraceEnabled()) LOG.trace("Deleted track : " + log.currentDeleted + "/" + log.getDeletedCount() + " id=" + trackId);
        }
        log.setTracksCount(TRACK_DAO.countByFolderId(folder.getId()));
        log.setStatus(FolderLog.Status.LOADED);
        TRACK_INFO_DAO.deleteOrphans();
        FOLDER_LOG_DAO.merge(log);
    }

    /**
	 * Scan file.
	 * @param file
	 *          the file
	 * @throws IOException
	 *           Signals that an I/O exception has occurred.
	 * @throws UnsupportedAudioFileException
	 *           the unsupported audio file exception
	 */
    @Transactional
    private void scanFile(final File file) throws UnsupportedAudioFileException, IOException {
        Track track;
        if (folder.getTracks().containsKey(file.getPath())) track = TRACK_DAO.findBy("path", file.getPath()); else track = new Track();
        track.setFolder(folder);
        track.setPath(file.getPath());
        track.setLastModified(new Date(file.lastModified()));
        final AudioFile audioFile = new AudioFile(file);
        track.setVBR(audioFile.getVBR());
        track.setTitle(audioFile.getTitle());
        track.setTrackNr(audioFile.getTrackNr());
        track.setDuration(audioFile.getDuration());
        track.setTrackInfo(audioFile.getArtistTrackInfo());
        track.setTrackInfo(audioFile.getGenreTrackInfo());
        track.setTrackInfo(audioFile.getYearTrackInfo());
        track.setTrackInfo(audioFile.getBitRateTrackInfo());
        track.setTrackInfo(audioFile.getAlbumTrackInfo());
        final Cover cover = audioFile.getAlbumCover();
        if (cover != null) COVER_DAO.save(cover);
        TRACK_DAO.save(track);
    }

    /**
	 * Parses the file.
	 */
    private void scanFiles() {
        LOG.info("Scanning files");
        setLogStatus(FolderLog.Status.SCANNING);
        for (final File file : files) try {
            log.currentScanned++;
            scanFile(file);
            if (LOG.isInfoEnabled()) LOG.trace("Updated track : " + log.currentScanned + "/" + log.getScannedCount() + " path:" + file.getPath());
        } catch (final Exception e) {
            log.setErrorCount(log.getErrorCount() + 1);
            if (LOG.isWarnEnabled()) LOG.warn("Error parsing File path:" + file.getPath() + " : " + e);
        }
        setLogStatus(FolderLog.Status.SCANNING_FINISHED);
    }

    /**
	 * Start scan.
	 */
    @Transactional
    private void startScan() {
        LOG.info("Starting Scan");
        folder = FOLDER_DAO.findById(folderId);
        log.setStatus(FolderLog.Status.LOADING);
        if (log.getId() == 0) log.setId(FOLDER_LOG_DAO.save(log).getId()); else log.setId(FOLDER_LOG_DAO.merge(log).getId());
        final Map<String, Track> tracks = folder.getTracks();
        if (tracks != null) {
            files = FileUtils.getFiles(new File(folder.getPath()), new UpdateFileFilter(tracks)).toArray(new File[] {});
            for (final Track track : tracks.values()) toDelete.add(track.getId());
        } else files = FileUtils.getFiles(new File(folder.getPath()), new FileExtensionFilter(extensions)).toArray(new File[] {});
        FileUtils.sortFiles(files);
        log.setDeletedCount(toDelete.size());
        log.setScannedCount(files.length);
        log.setStatus(FolderLog.Status.LOADING_FINISHED);
        FOLDER_LOG_DAO.merge(log);
    }
}
