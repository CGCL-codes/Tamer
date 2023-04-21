package net.sourceforge.symba.web.shared;

import net.sourceforge.symba.web.client.gui.InputValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Rather than storing the ExperimentSteps directly, store a Holder which contains the step, plus extra metadata
 * specific to its position in the list and to its original values (to show when something has changed).
 */
public class ExperimentStepHolder implements Serializable {

    private ExperimentStep original;

    private ExperimentStep current;

    private int stepId;

    private boolean isModified;

    @SuppressWarnings("unused")
    public ExperimentStepHolder() {
        this.original = new ExperimentStep();
        this.current = new ExperimentStep(this.original);
        this.stepId = 0;
        this.isModified = false;
    }

    public ExperimentStepHolder(ExperimentStep step) {
        this.original = new ExperimentStep(step);
        this.current = step;
        this.stepId = 0;
        this.isModified = false;
    }

    public ExperimentStep getCurrent() {
        return current;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    /**
     * adds an additional experimental step as a child of the step specified by selectedRow
     *
     * @param selectedRow the row identifier used to identify which step to add a child to
     * @return true if a row was added, false if no matching row was found.
     */
    public boolean addSubStepAtStepId(int selectedRow) {
        if (stepId == selectedRow) {
            ExperimentStep step = new ExperimentStep();
            step.createDatabaseId();
            current.addChild(step);
            setModified(true);
            return true;
        }
        if (!current.isLeaf()) {
            for (ExperimentStepHolder holder : current.getChildren()) {
                if (holder.addSubStepAtStepId(selectedRow)) {
                    setModified(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * adds a parameter triple to the step specified by selectedRow
     *
     * @param selectedRow the row identifier used to identify which step to add the parameter to
     * @param subject     the parameter name
     * @param predicate   the connection between the subject and object, defaults to hasValue
     * @param objectValue the "object" of the triple, the value that fits with the subject and predicate
     * @param unit        the optional units of the objectValue
     * @param type        the measurement type for the parameter.
     * @return true if a triple was added, false if no matching row was found.
     */
    public boolean addParameterAtStepId(int selectedRow, String subject, String predicate, String objectValue, String unit, InputValidator.MeasurementType type) {
        if (stepId == selectedRow) {
            current.getParameters().add(new ExperimentParameter(subject, predicate, objectValue, unit, type));
            setModified(true);
            return true;
        }
        if (!current.isLeaf()) {
            for (ExperimentStepHolder holder : current.getChildren()) {
                if (holder.addParameterAtStepId(selectedRow, subject, predicate, objectValue, unit, type)) {
                    setModified(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * sets the value of the "current" object to the value in the string. If title is null or empty, the value of
     * the "current" object is reset to the value in the original object.
     *
     * @param selectedRow the row whose title is to be changed
     * @param title       the new title
     * @param parameters  the new parameters (existing ones will be overwritten)
     * @param inputs      the new input materials to set the step to (these materials completely re-write existing)
     * @param outputs     the new output materials to set the step to (these materials completely re-write existing)
     * @param fileInfo    the new file name and file description for this step
     * @return the new title value in [0]; and true if anything is new in this step, false if everything matches the
     *         original, in [1] - this is important, as it isn't necessarily the same as the title parameter
     */
    public Object[] setInfoAtStepId(int selectedRow, String title, ArrayList<ExperimentParameter> parameters, ArrayList<Material> inputs, ArrayList<Material> outputs, HashMap<String, String> fileInfo) {
        Object[] values = new Object[2];
        if (stepId == selectedRow) {
            if (title == null || title.length() == 0) {
                current.setTitle(original.getTitle());
                setModified(false);
            } else if (!current.getTitle().equals(title.trim())) {
                current.setTitle(title.trim());
                setModified(true);
            }
            if (current.getParameters() != parameters) {
                setModified(true);
                current.getParameters().clear();
                for (ExperimentParameter parameter : parameters) {
                    if (parameter.getSubject().length() == 0 && parameter.getPredicate().length() == 0 && parameter.getObjectValue().length() == 0 && parameter.getUnit().length() == 0) {
                        continue;
                    }
                    current.getParameters().add(parameter);
                }
            }
            if (current.getInputMaterials() != inputs) {
                setModified(true);
                System.err.println("Set modified to true due to a input material modification");
                current.getInputMaterials().clear();
                current.getInputMaterials().addAll(inputs);
            }
            if (current.getOutputMaterials() != outputs) {
                setModified(true);
                current.getOutputMaterials().clear();
                current.getOutputMaterials().addAll(outputs);
            }
            if (current.getFileInfo() != fileInfo) {
                setModified(true);
                current.setFileInfo(fileInfo);
            }
            values[0] = current.getTitle();
            values[1] = isModified();
            return values;
        }
        if (!current.isLeaf()) {
            for (ExperimentStepHolder holder : current.getChildren()) {
                Object[] returnedValues = holder.setInfoAtStepId(selectedRow, title, parameters, inputs, outputs, fileInfo);
                if (returnedValues.length == 2 && returnedValues[0] != null && returnedValues[1] != null) {
                    setModified((Boolean) returnedValues[1]);
                    return returnedValues;
                }
            }
        }
        return values;
    }

    /**
     * Will add a copy of the step to the same array the selectedRow is in (i.e. a sibling)
     *
     * @param selectedRow the row to find a match with
     * @return the new ExperimentStep to add. If the ExperimentStep has already been added at the right place,
     *         then the return value will be null.
     */
    public ExperimentStep copyStepWithStepId(int selectedRow) {
        if (stepId == selectedRow) {
            ExperimentStep step = current.deepCopy();
            step.setTitle("Copy of " + step.getTitle());
            return step;
        }
        ExperimentStep step = null;
        ExperimentStepHolder holder;
        if (!current.isLeaf()) {
            for (int i = 0, childrenSize = current.getChildren().size(); i < childrenSize; i++) {
                holder = current.getChildren().get(i);
                step = holder.copyStepWithStepId(selectedRow);
                if (step != null) {
                    break;
                }
            }
            if (step != null) {
                System.err.println("copying at non-top level");
                current.addChild(step);
                setModified(true);
                return null;
            }
        }
        return null;
    }

    public void setAllModified(boolean value) {
        setModified(value);
        if (!current.isLeaf()) {
            for (int i = 0, childrenSize = current.getChildren().size(); i < childrenSize; i++) {
                current.getChildren().get(i).setAllModified(value);
            }
        }
    }

    public int setFileAtStepId(int selectedRow, int depth, String fileName, String fileDescription) {
        if (stepId == selectedRow) {
            if (fileName != null && fileName.length() > 0) {
                current.getFileInfo().put(fileName, fileDescription);
                setModified(true);
            }
            return depth;
        }
        if (!current.isLeaf()) {
            for (ExperimentStepHolder holder : current.getChildren()) {
                int returnedDepth = holder.setFileAtStepId(selectedRow, depth + 1, fileName, fileDescription);
                if (returnedDepth != -1) {
                    setModified(true);
                    return returnedDepth;
                }
            }
        }
        return -1;
    }

    public void clearFileAssociations() {
        current.getFileInfo().clear();
        original.getFileInfo().clear();
        if (!current.isLeaf()) {
            for (ExperimentStepHolder holder : current.getChildren()) {
                holder.clearFileAssociations();
            }
        }
        if (!original.isLeaf()) {
            for (ExperimentStepHolder holder : original.getChildren()) {
                holder.clearFileAssociations();
            }
        }
    }

    public void setFullyWriteableParameters(boolean writeable) {
        for (ExperimentParameter parameter : current.getParameters()) {
            parameter.setFullyWriteable(writeable);
        }
        for (ExperimentParameter parameter : original.getParameters()) {
            parameter.setFullyWriteable(writeable);
        }
        if (!current.isLeaf()) {
            for (ExperimentStepHolder holder : current.getChildren()) {
                holder.setFullyWriteableParameters(writeable);
            }
        }
        if (!original.isLeaf()) {
            for (ExperimentStepHolder holder : original.getChildren()) {
                holder.setFullyWriteableParameters(writeable);
            }
        }
    }
}
