package edu.harvard.fas.rregan.requel.project.command;

import edu.harvard.fas.rregan.requel.command.EditCommand;
import edu.harvard.fas.rregan.requel.project.ReportGenerator;

/**
 * @author ron
 */
public interface DeleteReportGeneratorCommand extends EditCommand {

    /**
	 * Set the reportGenerator to delete.
	 * 
	 * @param reportGenerator
	 */
    public void setReportGenerator(ReportGenerator reportGenerator);
}
