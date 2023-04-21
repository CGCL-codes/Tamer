package com.global360.sketchpadbpmn.commandmanager.commands;

import java.awt.Color;
import java.awt.Font;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.SketchpadException;
import com.global360.sketchpadbpmn.commandmanager.CommandManager;
import com.global360.sketchpadbpmn.documents.FontAlignment;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.documents.types.DataDirection;
import com.global360.sketchpadbpmn.graphic.BPMNDataGraphic;

/**
 * @author andya
 *
 */
public class DataGraphicChangeCommand extends GraphicChangeCommand {

    BPMNDataGraphic dataGraphic;

    String state, oldState;

    Boolean requiredForStart, oldRequiredForStart;

    Boolean producedAtCompletion, oldProducedAtCompletion;

    Boolean isCollection, oldIsCollection;

    DataDirection dataDirection, oldDataDirection;

    /**
   * @param sketchManager
   * @param commandManager
 * @param isCollection 
 * @param dataDirection 
   */
    public DataGraphicChangeCommand(SketchManager sketchManager, CommandManager commandManager, BPMNDataGraphic dataGraphic, BpmnId id, String name, double x, double y, double width, double height, Font font, FontAlignment alignment, Color lineColor, Color fillColor, Color textColor, String description, String state, Boolean requiredForStart, Boolean producedAtCompletion, Boolean isCollection, DataDirection dataDirection) {
        super(sketchManager, commandManager, dataGraphic, id, name, x, y, width, height, font, alignment, lineColor, fillColor, textColor, description);
        this.dataGraphic = dataGraphic;
        this.state = state;
        this.oldState = dataGraphic.getState();
        this.requiredForStart = requiredForStart;
        this.oldRequiredForStart = dataGraphic.getRequiredForStart();
        this.producedAtCompletion = producedAtCompletion;
        this.oldProducedAtCompletion = dataGraphic.getProducedAtCompletion();
        this.isCollection = isCollection;
        this.oldIsCollection = dataGraphic.getIsCollection();
        this.dataDirection = dataDirection;
        this.oldDataDirection = dataGraphic.getDirection();
    }

    @Override
    public void Do() throws SketchpadException {
        commandManager.getDocument().setDataNode(getPage(), dataGraphic, id, name, x, y, width, height, font, alignment, lineColor, fillColor, textColor, description, state, requiredForStart, producedAtCompletion, isCollection, dataDirection);
        repaintCanvas();
    }

    @Override
    public void Undo() throws SketchpadException {
        commandManager.getDocument().setDataNode(getPage(), dataGraphic, id, oldName, oldX, oldY, oldWidth, oldHeight, oldFont, oldAlignment, oldLineColor, oldFillColor, oldTextColor, oldDescription, oldState, oldRequiredForStart, oldProducedAtCompletion, oldIsCollection, oldDataDirection);
        repaintCanvas();
        getSelectionManager().set(this.getSelection());
    }

    @Override
    public boolean isNOP() {
        if (super.isNOP() && (state == oldState) && (requiredForStart == oldRequiredForStart) && (producedAtCompletion == oldProducedAtCompletion) && (isCollection == oldIsCollection) && (dataDirection == oldDataDirection)) {
            return true;
        }
        return false;
    }
}
