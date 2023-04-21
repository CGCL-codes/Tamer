package net.matmas.pneditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import net.matmas.pnapi.Marking;
import net.matmas.pnapi.Place;
import net.matmas.pneditor.MainFrame;
import net.matmas.pneditor.PNEditor;
import net.matmas.pneditor.Selection;
import net.matmas.pneditor.commands.CompositeCommand;
import net.matmas.pneditor.commands.SetTokensCommand;
import net.matmas.util.CollectionTools;
import net.matmas.pneditor.commands.Command;
import net.matmas.util.GraphicsTools;

/**
 *
 * @author matmas
 */
public class SetTokensAction extends Action {

    public SetTokensAction() {
        String name = "Set tokens";
        putValue(NAME, name);
        putValue(SMALL_ICON, GraphicsTools.getIcon("pneditor/tokens.gif"));
        putValue(SHORT_DESCRIPTION, name);
        putValue(MNEMONIC_KEY, KeyEvent.VK_T);
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        MainFrame mainFrame = PNEditor.getInstance().getMainFrame();
        Marking currentMarking = PNEditor.getInstance().getDocument().getPetriNet().getMarking();
        Selection selection = PNEditor.getInstance().getMainFrame().getDrawingBoard().getCanvas().getSelection();
        Set<Place> selectedPlaces = selection.getPlaces();
        if (!selectedPlaces.isEmpty()) {
            int tokens = 0;
            if (selectedPlaces.size() == 1) {
                Place place = CollectionTools.getFirstElement(selectedPlaces);
                tokens = currentMarking.getTokens(place);
            }
            String response = JOptionPane.showInputDialog(mainFrame, "Tokens:", tokens);
            if (response != null) {
                try {
                    tokens = Integer.parseInt(response);
                    if (tokens < 0) {
                        JOptionPane.showMessageDialog(mainFrame, "Number of tokens must be non-negative");
                    } else {
                        setTokens(selectedPlaces, tokens);
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(mainFrame, exception.getMessage() + " is not a number");
                }
            }
        }
    }

    private void setTokens(Set<Place> selectedPlaces, int tokens) {
        Marking currentMarking = PNEditor.getInstance().getDocument().getPetriNet().getMarking();
        List<Command> commands = new ArrayList<Command>();
        for (Place place : selectedPlaces) {
            if (currentMarking.getTokens(place) != tokens) {
                commands.add(new SetTokensCommand(place, tokens, currentMarking));
            }
        }
        if (!commands.isEmpty()) {
            PNEditor.getInstance().getUndoManager().executeCommand(new CompositeCommand(commands));
        }
    }

    @Override
    public boolean shouldBeEnabled() {
        Selection selection = PNEditor.getInstance().getMainFrame().getDrawingBoard().getCanvas().getSelection();
        return !selection.getPlaces().isEmpty();
    }
}
