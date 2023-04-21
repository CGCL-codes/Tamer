package net.sourceforge.solexatools.business;

import net.sourceforge.solexatools.model.Experiment;
import net.sourceforge.solexatools.model.FileType;
import net.sourceforge.solexatools.model.IUS;
import net.sourceforge.solexatools.model.Lane;
import net.sourceforge.solexatools.model.Processing;
import net.sourceforge.solexatools.model.Registration;
import net.sourceforge.solexatools.model.Sample;
import net.sourceforge.solexatools.model.SequencerRun;
import net.sourceforge.solexatools.model.Study;
import net.sourceforge.solexatools.util.UploadFile;

public interface FileUploadService {

    public static final String NAME = "FileUploadService";

    public void uploadFile(Study study, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;

    public void uploadFile(Experiment experiment, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;

    public void uploadFile(Sample sample, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;

    public void uploadFile(Lane lane, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;

    public void uploadFile(IUS ius, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;

    public void uploadFile(Processing processing, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;

    public void uploadFile(SequencerRun sequencerRun, UploadFile uploadFile, FileType fileType, Registration registration) throws Exception;
}
