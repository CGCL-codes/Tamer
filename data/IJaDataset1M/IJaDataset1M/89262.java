package com.stakface.ocmd.gui.plugins;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import com.stakface.ocmd.OCmd;
import com.stakface.ocmd.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.*;

public class FileWrapper extends JComponent implements MouseListener {

    public static final int PROPERTY_NAME = 0;

    public static final int PROPERTY_SIZE = 1;

    public static final int PROPERTY_DATE = 2;

    public static final int PROPERTY_INFO = 3;

    public static final int NUM_PROPERTIES = 4;

    private static final String DEF_DIRECTORY_SIZE = "<DIR>";

    private static int _iconSize;

    private static DateFormat _dateFormat = DateFormat.getInstance();

    private static NumberFormat _numberFormat = NumberFormat.getInstance();

    protected OCmd _ocmd;

    private DirectoryListing _callback;

    private File _file;

    private Icon _fileIcon;

    private String _displayName;

    private String _directorySize;

    private boolean _selected;

    private boolean _active;

    public FileWrapper(OCmd ocmd, DirectoryListing callback, File file) {
        this(ocmd, callback, file, null);
    }

    public FileWrapper(OCmd ocmd, DirectoryListing callback, File file, String displayName) {
        _ocmd = ocmd;
        _callback = callback;
        _file = file;
        _fileIcon = FileSystemView.getFileSystemView().getSystemIcon(_file);
        _displayName = displayName;
        _directorySize = DEF_DIRECTORY_SIZE;
        _iconSize = Utils.getMax(new int[] { _iconSize, _fileIcon.getIconWidth(), _fileIcon.getIconHeight() });
        _selected = false;
        _active = false;
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setOpaque(true);
        setFocusable(false);
        addMouseListener(this);
    }

    public File getFile() {
        return _file;
    }

    public static int getIconSize() {
        return _iconSize;
    }

    public void setDisplayName(String displayName) {
        _displayName = displayName;
        repaint();
    }

    public void setDirectorySize(long size) {
        _directorySize = (size < 0 ? DEF_DIRECTORY_SIZE : _numberFormat.format(size));
        repaint();
    }

    protected int getNumProperties() {
        return NUM_PROPERTIES;
    }

    protected String getFileProperty(int propertyIndex) {
        if (_file == null) {
            return "";
        }
        switch(propertyIndex) {
            case PROPERTY_NAME:
                return (_displayName == null ? _file.getName() : _displayName);
            case PROPERTY_SIZE:
                return (_file.isDirectory() ? _directorySize : _numberFormat.format(_file.length()));
            case PROPERTY_DATE:
                return _dateFormat.format(new Date(_file.lastModified()));
            case PROPERTY_INFO:
                return getFileInfo();
        }
        return "";
    }

    private String getFileInfo() {
        StringBuffer info = new StringBuffer();
        if (_file != null) {
            info.append(_file.isDirectory() ? 'd' : '-');
            info.append(_file.isHidden() ? 'h' : '-');
            info.append(_file.canRead() ? 'r' : '-');
            info.append(_file.canWrite() ? 'w' : '-');
        }
        return info.toString();
    }

    public void select() {
        _selected = true;
        _callback.addSelectedFile(_file);
        setBackground(Color.CYAN);
        repaint();
    }

    public void deselect() {
        _selected = false;
        _callback.removeSelectedFile(_file);
        setBackground(Color.WHITE);
        repaint();
    }

    public void toggleSelection() {
        if (_selected) {
            deselect();
        } else {
            select();
        }
    }

    public boolean isSelected() {
        return _selected;
    }

    void activate() {
        _active = true;
        repaint();
    }

    void deactivate() {
        _active = false;
        repaint();
    }

    public boolean isActive() {
        return _active;
    }

    public int getHeight() {
        return _iconSize + 2;
    }

    public int getWidth() {
        return _callback.getTotalWidth();
    }

    protected void paintBorder(Graphics gr) {
        if (!_active) {
            return;
        }
        Graphics2D g = (Graphics2D) gr.create();
        g.setColor(_callback.isFocusOwner() ? Color.RED : Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.dispose();
    }

    protected void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr.create();
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics();
        int baseline = (getHeight() / 2) + ((fm.getAscent() - fm.getDescent()) / 2);
        _fileIcon.paintIcon(this, g, (getHeight() - _fileIcon.getIconWidth()) / 2, (getHeight() - _fileIcon.getIconHeight()) / 2);
        int numProperties = getNumProperties();
        for (int i = 0; i < numProperties; i++) {
            String property = getFileProperty(i);
            g.drawString(property, _callback.getColumnOffset(i, fm.bytesWidth(property.getBytes(), 0, property.length())), baseline);
        }
        g.dispose();
    }

    public void mousePressed(MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            toggleSelection();
        }
        _callback.setActiveFileWrapper(this);
        _callback.requestFocus();
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
        switch(evt.getClickCount()) {
            case 1:
                break;
            case 2:
                _callback.downLevel(this);
                break;
        }
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    public boolean equals(Object o) {
        if (o instanceof FileWrapper) {
            return ((FileWrapper) o)._file.equals(_file);
        } else if (o instanceof File) {
            return ((File) o).equals(_file);
        }
        return false;
    }
}
