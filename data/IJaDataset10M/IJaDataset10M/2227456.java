package org.proteored.miapeapi.xml.mzidentml_1_1;

import java.util.HashMap;
import java.util.Set;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProteinSet;
import org.proteored.miapeapi.interfaces.msi.InputDataSet;
import org.proteored.miapeapi.interfaces.msi.InputParameter;

public class ProteinSetImpl implements IdentifiedProteinSet {

    private final InputParameter inputParameter;

    private final Set<InputDataSet> inputDataSets;

    private final HashMap<String, IdentifiedProtein> identifiedProteinList = new HashMap<String, IdentifiedProtein>();

    public ProteinSetImpl(InputParameter inputParameter, Set<InputDataSet> inputDataSets) {
        this.inputParameter = inputParameter;
        this.inputDataSets = inputDataSets;
    }

    @Override
    public String getFileLocation() {
        return null;
    }

    @Override
    public HashMap<String, IdentifiedProtein> getIdentifiedProteins() {
        if (!identifiedProteinList.isEmpty()) return identifiedProteinList;
        return null;
    }

    @Override
    public Set<InputDataSet> getInputDataSets() {
        return inputDataSets;
    }

    @Override
    public InputParameter getInputParameter() {
        return inputParameter;
    }

    @Override
    public String getName() {
        return "identified proteins";
    }

    public void addIdentifiedProtein(IdentifiedProtein protein) {
        identifiedProteinList.put(protein.getAccession(), protein);
    }
}
