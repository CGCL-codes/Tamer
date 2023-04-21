package com.global360.sketchpadbpmn.commandmanager.commands;

import java.awt.Color;
import java.awt.Font;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.Utility;
import com.global360.sketchpadbpmn.commandmanager.CommandManager;
import com.global360.sketchpadbpmn.documents.FontAlignment;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.graphic.SketchGraphic;

/**
 * @author andya
 *
 */
public abstract class GraphicChangeCommand extends Command {

    SketchGraphic graphic;

    BpmnId id, oldId;

    String name, oldName;

    double x, oldX;

    double y, oldY;

    double width, oldWidth;

    double height, oldHeight;

    Font font, oldFont;

    FontAlignment alignment, oldAlignment;

    String description, oldDescription;

    Color lineColor, oldLineColor;

    Color fillColor, oldFillColor;

    Color textColor, oldTextColor;

    /**
   * @param sketchManager
   * @param commandManager
   */
    public GraphicChangeCommand(SketchManager sketchManager, CommandManager commandManager, SketchGraphic graphic, BpmnId id, String name, double x, double y, double width, double height, Font font, FontAlignment alignment, Color newLineColor, Color newFillColor, Color newTextColor, String description) {
        super(sketchManager, commandManager);
        if (!Utility.areEqual(id, graphic.getId())) {
            Command.log.error("GraphicChangeCommand: id parameter: " + id + " differs from SketchGraphic id: " + graphic.getId());
        }
        this.graphic = graphic;
        this.id = id;
        this.name = name;
        this.oldName = graphic.getName();
        this.x = x;
        this.oldX = graphic.getX();
        this.y = y;
        this.oldY = graphic.getY();
        this.width = width;
        this.oldWidth = graphic.getWidth();
        this.height = height;
        this.oldHeight = graphic.getHeight();
        this.oldFont = graphic.getFontRaw();
        this.font = font;
        this.oldAlignment = graphic.getNameAlignment();
        this.alignment = alignment;
        this.lineColor = newLineColor;
        this.oldLineColor = graphic.getLineColorRaw();
        this.fillColor = newFillColor;
        this.oldFillColor = graphic.getFillColorRaw();
        this.textColor = newTextColor;
        this.oldTextColor = graphic.getTextColorRaw();
        this.description = description;
        this.oldDescription = graphic.getDescription();
        this.setSelection(this.getCurrentSelectionOfOriginals());
    }

    @Override
    public boolean isNOP() {
        if (Utility.areEqual(id, graphic.getId()) && Utility.areEqual(name, oldName) && (oldX == x) && (oldY == y) && (oldHeight == height) && (oldWidth == width) && (oldHeight == height) && Utility.areEqual(font, oldFont) && (oldAlignment == alignment) && Utility.areEqual(lineColor, oldLineColor) && Utility.areEqual(fillColor, oldFillColor) && Utility.areEqual(textColor, oldTextColor) && Utility.areEqual(description, oldDescription)) {
            return true;
        }
        return false;
    }
}
