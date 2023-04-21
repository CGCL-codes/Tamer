package com.iv.flash.commands;

import com.iv.flash.api.*;
import com.iv.flash.api.action.Program;
import com.iv.flash.api.button.ActionCondition;
import com.iv.flash.api.button.Button2;
import com.iv.flash.api.button.ButtonRecord;
import com.iv.flash.api.shape.FillStyle;
import com.iv.flash.api.shape.Shape;
import com.iv.flash.commands.GenericCommand;
import com.iv.flash.context.Context;
import com.iv.flash.context.ContextFactory;
import com.iv.flash.util.GeomHelper;
import com.iv.flash.util.IVException;
import com.iv.flash.util.Log;
import com.iv.flash.util.PropertyManager;
import com.iv.flash.util.UrlDataSource;
import com.iv.flash.util.Util;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/** MultiPage Lists JGenerator Object
 * @author  William L. Thomson Jr.
 * @company Obsidian-Studios Inc.
 */
public class ColumnedListCommand extends GenericCommand {

    protected boolean url_mask;

    protected int clip_column, url_column, window_column, columns, column_spacing, column_padding, rows, row_spacing, row_padding = -1;

    protected String data_source, instance_name, window;

    protected void initColumnIndexes(String[][] data) {
        clip_column = findColumn("Clip", data);
        url_column = findColumn("Url", data);
        window_column = findColumn("Window", data);
        if (clip_column == -1) Log.error("ColumnedListCommand Error\nA clip column was not specified in the data source.");
        if (url_column != -1 && window_column == -1) Log.warn("ColumnedListCommand Warning\nA url was specified but a window column was not specified in the data source. Default window, _blank, will be used");
    }

    protected void initParams(Context context) {
        data_source = getParameter(context, "data_source", "");
        instance_name = getParameter(context, "instance_name", "");
        columns = getIntParameter(context, "columns", 0);
        column_spacing = getIntParameter(context, "column_spacing", 0) * 20;
        column_padding = getIntParameter(context, "column_padding", 0) * 20;
        rows = getIntParameter(context, "rows", 0);
        row_spacing = getIntParameter(context, "row_spacing", 0) * 20;
        row_padding = getIntParameter(context, "row_padding", 0) * 20;
        url_mask = getBoolParameter(context, "url_mask", false);
        window = getParameter(context, "window", "_blank");
        if (data_source.equals("")) Log.error("ColumnedListCommand Error\nA data source was not specified in authoring environment.");
    }

    public void doCommand(FlashFile flash_file, Context context, Script parentScript, int frames) {
        initParams(context);
        String[][] data = null;
        try {
            UrlDataSource urlDataSource = new UrlDataSource(data_source, flash_file);
            data = urlDataSource.getData();
        } catch (IVException ive) {
            Log.error("ColumnedListCommand Error\nCould not Parse the datasource into a multi-dimensional array because :\n" + ive);
        } catch (IOException ioe) {
            Log.error("ColumnedListCommand Error\nCould not Parse the datasource into a multi-dimensional array because :\n" + ioe);
        }
        if (data == null) Log.error("ColumnedListCommand Error\nThe datasource it empty."); else {
            initColumnIndexes(data);
            makeList(flash_file, context, data);
        }
    }

    protected void makeList(FlashFile flash_file, Context context, String[][] data) {
        Instance instance = getInstance();
        instance.name = instance_name;
        double width = instance.matrix.getScaleX() * 2048;
        double height = instance.matrix.getScaleY() * 2048;
        GeomHelper.deScaleMatrix(instance.matrix);
        Script script = instance.copyScript();
        Frame frame = script.getFrameAt(0);
        frame.addStopAction();
        double y = -height / 2;
        double x = -width / 2;
        double clip_height = 0;
        double clip_width = 0;
        double page_height = 0;
        double page_width = 0;
        for (int row = 1; row < data.length; row++) {
            Script clip_script = flash_file.getScript(data[row][clip_column]).copyScript();
            Context clip_context = null;
            try {
                clip_context = ContextFactory.createContext(data, row);
            } catch (IVException ive) {
                Log.error("ColumnedListCommand Error\nCould not create a clip context :\n" + ive);
            }
            try {
                flash_file.processScript(clip_script, clip_context);
            } catch (IVException ive) {
                Log.error("ColumnedListCommand Error\nCould not process clip script and context :\n" + ive);
            }
            if (row_spacing == 0) clip_height = clip_script.getBounds().getHeight() + row_padding; else clip_height = row_spacing;
            if (column_spacing == 0) {
                if (clip_width == 0) clip_width = clip_script.getBounds().getWidth() + column_padding; else if (clip_width < clip_script.getBounds().getWidth()) clip_width = clip_script.getBounds().getWidth() + column_padding;
            } else clip_width = column_spacing;
            page_height += clip_height;
            if (page_height <= height) {
                if (url_mask) {
                    Button2 button2 = createButton(data, row, clip_script);
                    frame.addInstance(button2, 1, AffineTransform.getTranslateInstance(x, y), null);
                } else {
                    frame.addInstance(clip_script, 1, AffineTransform.getTranslateInstance(x, y), null);
                }
                y += clip_height;
            } else if (page_width < width) {
                page_width += clip_width;
                x += clip_width;
                y = -height / 2;
                page_height = clip_height;
                if (url_mask) {
                    Button2 button2 = createButton(data, row, clip_script);
                    frame.addInstance(button2, 1, AffineTransform.getTranslateInstance(x, y), null);
                } else {
                    frame.addInstance(clip_script, 1, AffineTransform.getTranslateInstance(x, y), null);
                }
                y += clip_height;
            }
        }
    }

    protected Button2 createButton(FlashDef flash_def) {
        Button2 button2 = new Button2();
        int states = ButtonRecord.Up | ButtonRecord.Over | ButtonRecord.Down;
        button2.addButtonRecord(new ButtonRecord(states, flash_def, 1, AffineTransform.getTranslateInstance(0, 0), CXForm.newIdentity(true)));
        Shape shape = new Shape();
        shape.setBounds(flash_def.getBounds());
        shape.setFillStyle0(FillStyle.newSolid(new Color(0, 0, 0)));
        shape.drawRectangle(flash_def.getBounds());
        button2.addButtonRecord(new ButtonRecord(ButtonRecord.HitTest, shape, 2, AffineTransform.getTranslateInstance(0, 0), CXForm.newIdentity(true)));
        button2.setTrackAs(Button2.TrackAsButton);
        return (button2);
    }

    protected Button2 createButton(String[][] data, int row, Script clip_script) {
        Button2 button2 = createButton(clip_script);
        Program program = new Program();
        if (window_column == -1) program.getURL(data[row][url_column], "_blank"); else program.getURL(data[row][url_column], data[row][window_column]);
        button2.addActionCondition(ActionCondition.onPress(program));
        return (button2);
    }
}
