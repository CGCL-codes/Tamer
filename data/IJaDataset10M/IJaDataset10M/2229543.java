package cross.datastructures.workflow;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.jdom.Element;
import cross.IConfigurable;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.pipeline.ICommandSequence;
import cross.event.IEventSource;

/**
 * Workflow models a sequence of produced IWorkflowResults, which usually are
 * files created by {@link cross.datastructures.workflow.IWorkflowElement}
 * objects.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public interface IWorkflow extends IEventSource<IWorkflowResult>, IConfigurable {

    /**
	 * Append IWorkflowResult to this IWorkflow instance.
	 * 
	 * @param iwr
	 */
    public abstract void append(IWorkflowResult iwr);

    /**
	 * Return the active ICommandSequence instance.
	 * 
	 * @return
	 */
    public abstract ICommandSequence getCommandSequence();

    /**
	 * Returns the currently active configuration for this workflow.
	 * 
	 * @return
	 */
    public abstract Configuration getConfiguration();

    /**
	 * Returns the name of this IWorkflow.
	 * 
	 * @return
	 */
    public abstract String getName();

    /**
	 * Returns an iterator over all currently available results.
	 * 
	 * @return
	 */
    public abstract Iterator<IWorkflowResult> getResults();

    /**
	 * Returns the results for a specific IFileFragment.
	 * 
	 * @param iff
	 * @return
	 */
    public abstract List<IWorkflowResult> getResultsFor(IFileFragment iff);

    /**
	 * Returns the results created by a specific AFragmentCommand.
	 * 
	 * @param afc
	 * @return
	 */
    public abstract List<IWorkflowResult> getResultsFor(IWorkflowElement afc);

    /**
	 * Returns the results created by a specific AFragmentCommand for
	 * IFileFragment.
	 * 
	 * @param afc
	 * @param iff
	 * @return
	 */
    public abstract List<IWorkflowResult> getResultsFor(IWorkflowElement afc, IFileFragment iff);

    /**
	 * Returns the list of results matching a given file extension pattern.
	 * 
	 * @param fileExtension
	 * @return
	 */
    public abstract List<IWorkflowResult> getResultsOfType(String fileExtension);

    /**
	 * Returns the list of results matching a given file extension pattern
	 * created by a given IWorkflow implementation.
	 * 
	 * @param fileExtension
	 * @return
	 */
    public abstract List<IWorkflowResult> getResultsOfType(IWorkflowElement afc, String fileExtension);

    /**
	 * Return the startup data of this IWorkflow.
	 * 
	 * @return
	 */
    public abstract Date getStartupDate();

    /**
	 * Restore state of this IWorkflow instance from an XML document.
	 * 
	 * @param e
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public abstract void readXML(Element e) throws IOException, ClassNotFoundException;

    /**
	 * Save this IWorkflow.
	 */
    public abstract void save();

    /**
	 * Set ics to be the active ICommandSequence instance.
	 * 
	 * @param ics
	 */
    public abstract void setCommandSequence(ICommandSequence ics);

    /**
	 * Set the currently active configuration.
	 * 
	 * @param configuration
	 */
    public abstract void setConfiguration(Configuration configuration);

    /**
	 * Set the name of this IWorkflow instance.
	 * 
	 * @param name
	 */
    public abstract void setName(String name);

    /**
	 * Set the startup date of this IWorkflow instance.
	 * 
	 * @param date
	 */
    public abstract void setStartupDate(Date date);

    /**
	 * Write the state of this object to XML.
	 * 
	 * @return
	 * @throws IOException
	 */
    public abstract Element writeXML() throws IOException;

    /**
	 * Returns the output directory for the given object.
	 * 
	 * @param iwe
	 * @return
	 */
    public abstract File getOutputDirectory(Object iwe);
}
