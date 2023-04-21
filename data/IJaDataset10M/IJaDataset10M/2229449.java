package org.jcvi.vics.compute.service.metageno.anno;

import org.jcvi.vics.compute.engine.data.IProcessData;
import org.jcvi.vics.compute.engine.data.MissingDataException;
import org.jcvi.vics.compute.engine.service.ServiceException;
import org.jcvi.vics.compute.service.metageno.MetaGenoPerlConfig;
import org.jcvi.vics.compute.service.metageno.SimpleGridJobRunner;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Mar 19, 2009
 * Time: 2:33:56 PM
 */
public class MgTmhmmService extends MgAnnoBaseService {

    public static final String ANNOTATION_INPUT_DATA_TYPE = "TMHMMBSML";

    public void execute(IProcessData processData) throws ServiceException {
        try {
            init(processData);
            logger.info(getClass().getName() + " execute() start");
            File tmHmmCmdFile = new File(tmHmmCmd);
            if (!tmHmmCmdFile.exists()) {
                throw new ServiceException("Could not locate specificed TmHmmCmd file=" + tmHmmCmd);
            }
            String tmStr = tmHmmCmd + " " + inputFile.getAbsolutePath() + " > " + resultFile.getAbsolutePath();
            SimpleGridJobRunner job = new SimpleGridJobRunner(workingDir, tmStr, queue, parentTask.getParameter("project"), parentTask.getObjectId());
            if (!job.execute()) {
                throw new Exception("Grid job failed with cmd=" + tmStr);
            }
            String bsmlStr = MetaGenoPerlConfig.getCmdPrefix() + tmHmmBsmlCmd + " --input " + resultFile.getAbsolutePath() + " --output " + resultFile.getAbsolutePath() + ".bsml" + " --fasta_input " + inputFile.getAbsolutePath() + " --compress_bsml_output 0 " + " --id_repository " + getIdRepositoryDir().getAbsolutePath();
            job = new SimpleGridJobRunner(workingDir, bsmlStr, queue, parentTask.getParameter("project"), parentTask.getObjectId());
            if (!job.execute()) {
                throw new Exception("Grid job failed with cmd=" + bsmlStr);
            }
            File parseDir = new File(resultFile.getAbsolutePath() + "_tmhmmParseDir");
            parseDir.mkdirs();
            File parsedFile = new File(resultFile.getAbsolutePath() + ".bsml.parsed");
            String parserStr = MetaGenoPerlConfig.getCmdPrefix() + parserCmd + " --input_file " + resultFile.getAbsolutePath() + ".bsml" + " --input_type " + ANNOTATION_INPUT_DATA_TYPE + " " + " --output_file " + parsedFile.getAbsolutePath() + " --work_dir " + parseDir.getAbsolutePath();
            job = new SimpleGridJobRunner(workingDir, parserStr, queue, parentTask.getParameter("project"), parentTask.getObjectId());
            if (!job.execute()) {
                throw new Exception("Grid job failed with cmd=" + parserStr);
            }
            addParsedFile(parsedFile);
            File[] parseFiles = parseDir.listFiles();
            for (File f : parseFiles) {
                f.delete();
            }
            parseDir.delete();
            logger.info(getClass().getName() + " execute() finish");
        } catch (Exception e) {
            if (parentTaskErrorFlag) {
                logger.info("Parent task has error - returning");
            } else {
                this.setParentTaskToErrorStatus(parentTask, this.getClass().getName() + " : " + e.getMessage());
                throw new ServiceException(e);
            }
        }
    }

    protected void init(IProcessData processData) throws MissingDataException, IOException {
        super.init(processData);
        setup(getClass().getSimpleName(), ".tmhmm_out");
    }
}
