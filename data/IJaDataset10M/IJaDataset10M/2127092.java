package net.sourceforge.solexatools.business.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.solexatools.business.FileUploadService;
import net.sourceforge.solexatools.dao.ExperimentDAO;
import net.sourceforge.solexatools.dao.FileDAO;
import net.sourceforge.solexatools.dao.FileTypeDAO;
import net.sourceforge.solexatools.dao.IUSDAO;
import net.sourceforge.solexatools.dao.LaneDAO;
import net.sourceforge.solexatools.dao.ProcessingDAO;
import net.sourceforge.solexatools.dao.SampleDAO;
import net.sourceforge.solexatools.dao.SequencerRunDAO;
import net.sourceforge.solexatools.dao.StudyDAO;
import net.sourceforge.solexatools.model.Experiment;
import net.sourceforge.solexatools.model.File;
import net.sourceforge.solexatools.model.FileType;
import net.sourceforge.solexatools.model.IUS;
import net.sourceforge.solexatools.model.Lane;
import net.sourceforge.solexatools.model.Processing;
import net.sourceforge.solexatools.model.Registration;
import net.sourceforge.solexatools.model.Sample;
import net.sourceforge.solexatools.model.SequencerRun;
import net.sourceforge.solexatools.model.Study;
import net.sourceforge.solexatools.util.UploadFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploadServiceImpl implements FileUploadService {

    private StudyDAO studyDAO = null;

    private ExperimentDAO experimentDAO = null;

    private SampleDAO sampleDAO = null;

    private LaneDAO laneDAO = null;

    private ProcessingDAO processingDAO = null;

    private SequencerRunDAO sequencerRunDAO = null;

    private IUSDAO IUSDAO = null;

    private FileTypeDAO fileTypeDAO = null;

    private FileDAO fileDAO = null;

    private static final Log log = LogFactory.getLog(FileTypeServiceImpl.class);

    public FileUploadServiceImpl() {
        super();
    }

    public void setStudyDAO(StudyDAO studyDAO) {
        this.studyDAO = studyDAO;
    }

    public void setExperimentDAO(ExperimentDAO experimentDAO) {
        this.experimentDAO = experimentDAO;
    }

    public void setSampleDAO(SampleDAO sampleDAO) {
        this.sampleDAO = sampleDAO;
    }

    public void setLaneDAO(LaneDAO laneDAO) {
        this.laneDAO = laneDAO;
    }

    public void setProcessingDAO(ProcessingDAO processingDAO) {
        this.processingDAO = processingDAO;
    }

    public void setSequencerRunDAO(SequencerRunDAO sequencerRunDAO) {
        this.sequencerRunDAO = sequencerRunDAO;
    }

    public void setIUSDAO(IUSDAO IUSDAO) {
        this.IUSDAO = IUSDAO;
    }

    public void setFileTypeDAO(FileTypeDAO fileTypeDAO) {
        this.fileTypeDAO = fileTypeDAO;
    }

    /**
	 * Sets a private member variable with an instance of
	 * an implementation of FileDAO. This method
	 * is called by the Spring framework at run time.
	 *
	 * @param		fileDAO implementation of FileDAO
	 * @see			fileDAO
	 */
    public void setFileDAO(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    private File createFile(UploadFile uploadFile, String metaType, Registration owner) throws IOException {
        String filePath = "";
        if (uploadFile.getUseURL()) {
            filePath = uploadFile.getFileURL();
        } else {
            java.io.File f = fileDAO.saveFile(uploadFile.getFile(), uploadFile.getFolderStore(), owner);
            filePath = f.getPath();
        }
        File file = new File();
        file.setFilePath(filePath);
        file.setOwner(owner);
        file.setMetaType(metaType);
        fileDAO.insert(file);
        return file;
    }

    private Processing insertProcessing(Registration owner, Set<File> files) {
        Processing newProcessing = new Processing();
        newProcessing.setOwner(owner);
        newProcessing.setFiles(files);
        newProcessing.setStatus("success");
        newProcessing.setExitStatus(0);
        newProcessing.setProcessExitStatus(0);
        newProcessing.setRunStartTimestamp(null);
        newProcessing.setRunStopTimestamp(null);
        newProcessing.setAlgorithm("upload");
        newProcessing.setCreateTimestamp(new Date());
        processingDAO.insert(newProcessing);
        return newProcessing;
    }

    private Processing insert(UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Set<File> files = new TreeSet<File>();
        files.add(createFile(uploadFile, fileType.getMetaType(), registration));
        Processing newProcessing = insertProcessing(registration, files);
        return newProcessing;
    }

    @Override
    public void uploadFile(Study study, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = study.getProcessings();
        oldProcessings.add(newProcessing);
        newProcessing.getStudies().add(study);
        studyDAO.update(study);
    }

    @Override
    public void uploadFile(Experiment experiment, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = experiment.getProcessings();
        oldProcessings.add(newProcessing);
        newProcessing.getExperiments().add(experiment);
        experimentDAO.update(experiment);
    }

    @Override
    public void uploadFile(Sample sample, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = sample.getProcessings();
        oldProcessings.add(newProcessing);
        newProcessing.getSamples().add(sample);
        sampleDAO.update(sample);
    }

    @Override
    public void uploadFile(Lane lane, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = lane.getProcessings();
        oldProcessings.add(newProcessing);
        newProcessing.getLanes().add(lane);
        laneDAO.update(lane);
    }

    @Override
    public void uploadFile(IUS ius, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = ius.getProcessings();
        oldProcessings.add(newProcessing);
        newProcessing.getIUS().add(ius);
        IUSDAO.update(ius);
    }

    @Override
    public void uploadFile(Processing processing, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = processing.getChildren();
        oldProcessings.add(newProcessing);
        newProcessing.getParents().add(processing);
        processingDAO.update(processing);
    }

    @Override
    public void uploadFile(SequencerRun sequencerRun, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception {
        Processing newProcessing = insert(uploadFile, fileType, registration);
        Set<Processing> oldProcessings = sequencerRun.getProcessings();
        oldProcessings.add(newProcessing);
        newProcessing.getSequencerRuns().add(sequencerRun);
        sequencerRunDAO.update(sequencerRun);
    }
}
