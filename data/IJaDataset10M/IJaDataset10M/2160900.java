package genomemap.cef.command.listener;

import cef.command.Command;
import cef.command.CommandEvent;
import cef.command.listener.CommandListenerException;
import cef.command.listener.CommandPrintWriter;
import commons.util.Util;
import genomemap.cef.command.ProcessComplDataCommand;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @since Jun 12, 2011
 * @author Susanta Tewari
 */
public class ProcessComplDataCommandPrintWriter extends CommandPrintWriter {

    @Override
    public void receivedEvent(CommandEvent cmdEvent) throws CommandListenerException {
        super.receivedEvent(cmdEvent);
        ProcessComplDataCommand command = (ProcessComplDataCommand) cmdEvent.getSource();
        Map<Integer, Map<String, Set<String>>> assignments = command.getAssignments();
        Element element = createAssignmentXML(assignments);
        try {
            Util.XML.printXMLOutput(element, getPrintWriter());
            getPrintWriter().println();
        } catch (IOException ex) {
            throw new CommandListenerException("Error printing results", ex);
        }
        Set<String> genes_not_found = command.getGenesNotFound();
        getPrintWriter().println("Input-assignment-genes not found in organism data." + " Count: " + genes_not_found.size() + " Genes: " + genes_not_found);
        Set<String> genes_with_empty_clones = command.getGenesWithEmptyClones();
        getPrintWriter().println("Input-assignment-genes without clones in organism data." + " Count: " + genes_with_empty_clones.size() + " Genes: " + genes_with_empty_clones);
        getPrintWriter().println("Total assignment count: " + command.getTotalAssignmentCount());
        getPrintWriter().flush();
    }

    @Override
    public Class<? extends Command> getTargetCommandClass() {
        return ProcessComplDataCommand.class;
    }

    private Element createAssignmentXML(Map<Integer, Map<String, Set<String>>> assignments) {
        Element assignments_element = DocumentHelper.createElement("assignments");
        for (Integer linkageGroup : assignments.keySet()) {
            Element ch_element = DocumentHelper.createElement("ch");
            ch_element.addAttribute("id", linkageGroup.toString());
            Map<String, Set<String>> linkageAssignments = assignments.get(linkageGroup);
            for (String gene : linkageAssignments.keySet()) {
                Element assignment_element = DocumentHelper.createElement("assignment");
                assignment_element.addAttribute("gene", gene);
                assignment_element.addText(linkageAssignments.get(gene).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\,", ""));
                ch_element.add(assignment_element);
            }
            assignments_element.add(ch_element);
        }
        return assignments_element;
    }
}
