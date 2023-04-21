package org.tuba.spatschorke.diploma.repository.mock.views.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.tuba.editorinterface.ArtefactIntegrator;
import org.tuba.editorinterface.ArtefactLocation;
import org.tuba.exceptions.ParseException;
import org.tuba.spatschorke.diploma.repository.mock.Activator;
import org.tuba.spatschorke.diploma.repository.mock.util.Utility;
import org.tuba.spatschorke.diploma.repository.mock.views.MockRepositoryView;
import org.tuba.spatschorke.diploma.repository.mock.views.data.ModelTreeObject;
import org.tuba.spatschorke.diploma.repository.mock.views.data.TreeObject;

public class AddToOOAction extends Action {

    private MockRepositoryView view;

    public AddToOOAction(MockRepositoryView view) {
        super();
        this.view = view;
        String text = "add to OpenOffice";
        setText(text);
        setToolTipText(text);
        setImageDescriptor(Utility.getImageDescriptor("icons/add.gif"));
    }

    @Override
    public void run() {
        List<ArtefactLocation> modelLocations = new ArrayList<ArtefactLocation>();
        for (TreeObject selection : view.getSelectedObjects()) {
            if (selection instanceof ModelTreeObject) {
                String modelID = ((ModelTreeObject) selection).getModelID();
                modelLocations.add(new ArtefactLocation(Activator.PLUGIN_ID, modelID));
            }
        }
        ArtefactIntegrator ooIntegrator = new ArtefactIntegrator();
        try {
            ooIntegrator.insertArtefactReferences(modelLocations);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
