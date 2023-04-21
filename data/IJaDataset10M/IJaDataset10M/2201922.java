package sheep.view.Component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D.Float;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ListIterator;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import sheep.controller.Workspace;
import sheep.model.Note;

/**
 *
 * @author geek
 */
public class WeekGridPanel extends JPanel {

    private int line, column;

    private int columnWidth;

    private float beginTime = 7.f;

    private float endTime = 19.f;

    private float beginLunchTime = 12.f;

    private float endLunchTime = 13.5f;

    private int[] ArrowX, beginArrowY, endArrowY, beginLunchArrowY, endLunchArrowY;

    private int[] resizeArrowX, resizeArrowY;

    private GradientPaint p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, getHeight() - 1, new Color(100, 100, 100));

    private GradientPaint p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, getHeight() - 3, new Color(255, 255, 255, 100));

    private GradientPaint p4 = new GradientPaint(0, 1, new Color(200, 200, 0, 80), 0, getHeight() - 3, new Color(255, 255, 0, 100));

    private boolean dragBegin = false;

    private boolean dragEnd = false;

    private boolean dragBeginLunch = false;

    private boolean dragEndLunch = false;

    private Float r2d1, r2d2, r2d3, r2d4;

    private Shape clip1;

    private Graphics2D g2d;

    private int pas = 40;

    private JPanel notePanel;

    private JMenuItem ds = new JMenuItem("Nouvelle note");

    JMenuItem es = new JMenuItem("Editer note");

    JMenuItem ss = new JMenuItem("supprimer note");

    JPopupMenu popup = new JPopupMenu();

    private Workspace workSpace;

    public LinkedList<Note> noteList = new LinkedList<Note>();

    public LinkedList<Note> sortedList;

    GradientPaint gradient;

    public WeekGridPanel(int line, int column, Workspace newWorkSpace) {
        this.workSpace = newWorkSpace;
        noteList = workSpace.getNoteList();
        this.line = line;
        this.column = column;
        setOpaque(false);
        ArrowX = new int[3];
        beginArrowY = new int[3];
        endArrowY = new int[3];
        beginLunchArrowY = new int[3];
        endLunchArrowY = new int[3];
        resizeArrowX = new int[3];
        resizeArrowY = new int[3];
        ArrowX[0] = columnWidth;
        ArrowX[1] = columnWidth - 10;
        ArrowX[2] = columnWidth - 10;
        beginArrowY[0] = 20 + (int) (beginTime * pas);
        beginArrowY[1] = 30 + (int) (beginTime * pas);
        beginArrowY[2] = 10 + (int) (beginTime * pas);
        endArrowY[0] = 20 + (int) (endTime) * pas;
        endArrowY[1] = 30 + (int) (endTime) * pas;
        endArrowY[2] = 10 + (int) (endTime) * pas;
        beginLunchArrowY[0] = 20 + (int) (beginLunchTime * pas);
        beginLunchArrowY[1] = 30 + (int) (beginLunchTime * pas);
        beginLunchArrowY[2] = 10 + (int) (beginLunchTime * pas);
        endLunchArrowY[0] = 20 + (int) (endLunchTime) * pas;
        endLunchArrowY[1] = 30 + (int) (endLunchTime) * pas;
        endLunchArrowY[2] = 10 + (int) (endLunchTime) * pas;
        popup.add(ds);
        es.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                notePanel.setVisible(true);
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        ss.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                System.out.println("suppression ");
                System.out.println("suppression " + workSpace.getSelectedNote());
                workSpace.getSelectedNote().getCalendrier().getNoteList().remove(workSpace.getSelectedNote());
                noteList.remove(workSpace.getSelectedNote());
                sortedList.remove(workSpace.getSelectedNote());
                workSpace.getNoteList().remove(workSpace.getSelectedNote());
                workSpace.setSelectedNote(null);
                repaint();
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        ds.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                Note note3 = new Note();
                note3.setCalendrier(workSpace.getCurrentCalendrier());
                workSpace.getCurrentCalendrier().addNote(note3);
                int xPos = (int) (popup.getLocationOnScreen().getX() - WeekGridPanel.this.getLocationOnScreen().getX()) / columnWidth;
                int yPos = (int) (popup.getLocationOnScreen().getY() - WeekGridPanel.this.getLocationOnScreen().getY() - 20) / 40;
                Calendar temp = Calendar.getInstance();
                temp.setTime(workSpace.getCurrentDay().getTime());
                temp.add(Calendar.DAY_OF_YEAR, -((temp.get(Calendar.DAY_OF_WEEK) + 5) % 7) + xPos - 1);
                temp.set(Calendar.HOUR_OF_DAY, (int) (yPos));
                temp.set(Calendar.MINUTE, ((int) ((popup.getLocationOnScreen().getY() - WeekGridPanel.this.getLocationOnScreen().getY() - 20) % 40) < 10 ? 0 : ((popup.getLocationOnScreen().getY() - WeekGridPanel.this.getLocationOnScreen().getY() - 20) % 40) < 20 ? 15 : ((popup.getLocationOnScreen().getY() - WeekGridPanel.this.getLocationOnScreen().getY() - 20) % 40) < 30 ? 30 : 45));
                System.out.println("New note Week" + temp.getTime().toString());
                Calendar temp2 = Calendar.getInstance();
                temp2.setTime(temp.getTime());
                temp2.add(Calendar.HOUR_OF_DAY, 1);
                note3.setDate(temp);
                note3.setEndDate(temp2);
                System.out.println("création note :" + temp.getTime().toString().substring(0, 10) + " : " + temp2.getTime().toString().substring(0, 10));
                note3.setTitle("Nouvelle note");
                note3.setSelected(true);
                note3.setDragBottomEnable(true);
                workSpace.setSelectedNote(note3);
                noteList.add(note3);
                popup.setVisible(false);
                repaint();
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent e) {
                int Ymove = e.getY() - e.getY() % (pas / 4);
                int Xmove = e.getX() - e.getX() % columnWidth;
                if (dragBegin) {
                    beginArrowY[0] = Ymove;
                    beginArrowY[1] = Ymove + 10;
                    beginArrowY[2] = Ymove - 10;
                } else if (dragEnd) {
                    endArrowY[0] = Ymove;
                    endArrowY[1] = Ymove + 10;
                    endArrowY[2] = Ymove - 10;
                } else if (dragBeginLunch) {
                    beginLunchArrowY[0] = Ymove;
                    beginLunchArrowY[1] = Ymove + 10;
                    beginLunchArrowY[2] = Ymove - 10;
                } else if (dragEndLunch) {
                    endLunchArrowY[0] = Ymove;
                    endLunchArrowY[1] = Ymove + 10;
                    endLunchArrowY[2] = Ymove - 10;
                } else {
                    if (workSpace.getSelectedNote() != null && e.getX() >= columnWidth && e.getX() <= 8 * columnWidth) if (workSpace.getSelectedNote().isDragTopEnable()) {
                        if (e.getY() <= workSpace.getSelectedNote().getNoteZone().getY() + workSpace.getSelectedNote().getNoteZone().height - 10.) {
                            workSpace.getSelectedNote().getNoteZone().height = workSpace.getSelectedNote().getNoteZone().height - Ymove + workSpace.getSelectedNote().getNoteZone().y;
                            workSpace.getSelectedNote().getNoteZone().y = Ymove;
                            workSpace.getSelectedNote().getDate().set(Calendar.HOUR_OF_DAY, (e.getY() - 20) / 40);
                            workSpace.getSelectedNote().getDate().set(Calendar.MINUTE, (((e.getY() - 20) % 40) < 10 ? 0 : ((e.getY() - 20) % 40) < 20 ? 15 : ((e.getY() - 20) % 40) < 30 ? 30 : 45));
                        }
                    } else if (workSpace.getSelectedNote().isDragBottomEnable()) {
                        if (workSpace.getSelectedNote().getNoteZone() != null) if (e.getY() >= workSpace.getSelectedNote().getNoteZone().getY() + 20.) {
                            workSpace.getSelectedNote().getNoteZone().height = Ymove - workSpace.getSelectedNote().getNoteZone().y;
                            workSpace.getSelectedNote().getEndDate().set(Calendar.HOUR_OF_DAY, (int) (e.getY() - 20) / 40);
                            workSpace.getSelectedNote().getEndDate().set(Calendar.MINUTE, (int) (((e.getY() - 20) % 40) < 10 ? 0 : ((e.getY() - 20) % 40) < 20 ? 15 : ((e.getY() - 20) % 40) < 30 ? 30 : 45));
                        }
                    } else if (workSpace.getSelectedNote().isDragEnable()) {
                        int i = workSpace.getSelectedNote().getDate().get(Calendar.HOUR_OF_DAY);
                        workSpace.getSelectedNote().getNoteZone().y = Ymove;
                        workSpace.getSelectedNote().getNoteZone().x = Xmove;
                        workSpace.getSelectedNote().setXPos(e.getX() / columnWidth);
                        workSpace.getSelectedNote().setYPos(e.getY() / 20);
                        workSpace.getSelectedNote().getDate().set(Calendar.DAY_OF_WEEK, ((Xmove) / columnWidth + 1) % 7);
                        workSpace.getSelectedNote().getDate().set(Calendar.HOUR_OF_DAY, (e.getY() - pas / 2) / pas);
                        workSpace.getSelectedNote().getDate().set(Calendar.MINUTE, (((e.getY() - pas / 2) % pas) < 10 ? 0 : ((e.getY() - pas / 2) % pas) < 20 ? 15 : ((e.getY() - pas / 2) % pas) < 30 ? 30 : 45));
                        workSpace.getSelectedNote().getEndDate().set(Calendar.DAY_OF_WEEK, ((Xmove) / columnWidth + 1) % 7);
                        workSpace.getSelectedNote().getEndDate().set(Calendar.HOUR_OF_DAY, (int) (e.getY() - 20 + workSpace.getSelectedNote().getNoteZone().height) / 40);
                        workSpace.getSelectedNote().getEndDate().set(Calendar.MINUTE, (int) (((e.getY() - 20 + workSpace.getSelectedNote().getNoteZone().height) % 40) < 10 ? 0 : ((e.getY() - 20 + workSpace.getSelectedNote().getNoteZone().height) % 40) < 20 ? 15 : ((e.getY() - 20 + workSpace.getSelectedNote().getNoteZone().height) % 40) < 30 ? 30 : 45));
                    }
                }
                if (sortedList != null) {
                    ListIterator iter = sortedList.listIterator(sortedList.size());
                    while (iter.hasPrevious()) {
                        Note testNote = (Note) iter.previous();
                        int i = testNote.getDate().get(Calendar.DAY_OF_YEAR) - workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) + ((workSpace.getCurrentDay().get(Calendar.DAY_OF_WEEK) + 5) % 7);
                        if (i < 7 && i >= 0) if (testNote.getNoteZone() != null && testNote.getCalendrier().isDisplay()) if (e.getX() >= testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 && e.getX() <= testNote.getNoteZone().getX() + columnWidth && e.getY() >= testNote.getNoteZone().getY() - 5 && e.getY() <= testNote.getNoteZone().getY() + 5) {
                            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                            break;
                        } else if (e.getX() >= testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 && e.getX() <= testNote.getNoteZone().getX() + columnWidth && e.getY() >= testNote.getNoteZone().getY() + testNote.getNoteZone().height - 5 && e.getY() <= testNote.getNoteZone().getY() + testNote.getNoteZone().height + 5) {
                            setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                            break;
                        } else if (e.getX() >= testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 && e.getX() <= testNote.getNoteZone().getX() + columnWidth && e.getY() > testNote.getNoteZone().getY() + 5 && e.getY() < testNote.getNoteZone().getY() + testNote.getNoteZone().height - 5) {
                            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                            break;
                        } else {
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
                repaint();
            }

            public void mouseDragged(MouseEvent e) {
            }
        });
        this.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                popup.remove(es);
                popup.remove(ss);
                System.out.println(" day week click" + workSpace.getCurrentDay().getTime().toString());
                if (SwingUtilities.isRightMouseButton(e) && e.getX() >= columnWidth) {
                    if (workSpace.getSelectedNote() != null) {
                        if (!(workSpace.getSelectedNote().isDragEnable() || workSpace.getSelectedNote().isDragBottomEnable() || workSpace.getSelectedNote().isDragTopEnable() || dragBeginLunch || dragEndLunch || dragBegin || dragEnd)) {
                            if (sortedList != null) {
                                ListIterator iter = sortedList.listIterator(sortedList.size());
                                while (iter.hasPrevious()) {
                                    Note testNote = (Note) iter.previous();
                                    int i = testNote.getDate().get(Calendar.DAY_OF_YEAR) - workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) + ((workSpace.getCurrentDay().get(Calendar.DAY_OF_WEEK) + 5) % 7);
                                    if (i < 7 && i >= 0) if (testNote.getNoteZone() != null && testNote.getCalendrier().isDisplay()) if (e.getX() >= testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 && e.getX() <= testNote.getNoteZone().getX() + columnWidth && e.getY() >= testNote.getNoteZone().getY() && e.getY() <= testNote.getNoteZone().getY() + testNote.getNoteZone().getHeight()) {
                                        workSpace.setSelectedNote(testNote);
                                        testNote.setSelected(true);
                                        popup.add(es);
                                        popup.add(ss);
                                    }
                                }
                            }
                            popup.setLocation((int) (WeekGridPanel.this.getLocationOnScreen().getX() + e.getX()), (int) (WeekGridPanel.this.getLocationOnScreen().getY() + e.getY()));
                            popup.setVisible(true);
                        }
                    } else {
                        if (sortedList != null) {
                            ListIterator iter = sortedList.listIterator(sortedList.size());
                            while (iter.hasPrevious()) {
                                Note testNote = (Note) iter.previous();
                                int i = testNote.getDate().get(Calendar.DAY_OF_YEAR) - workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) + ((workSpace.getCurrentDay().get(Calendar.DAY_OF_WEEK) + 5) % 7);
                                if (i < 7 && i >= 0) if (testNote.getNoteZone() != null && testNote.getCalendrier().isDisplay()) if (e.getX() >= testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 && e.getX() <= testNote.getNoteZone().getX() + columnWidth && e.getY() >= testNote.getNoteZone().getY() && e.getY() <= testNote.getNoteZone().getY() + testNote.getNoteZone().getHeight()) {
                                    workSpace.setSelectedNote(testNote);
                                    testNote.setSelected(true);
                                    popup.add(es);
                                    popup.add(ss);
                                }
                            }
                        }
                        popup.setLocation((int) (WeekGridPanel.this.getLocationOnScreen().getX() + e.getX()), (int) (WeekGridPanel.this.getLocationOnScreen().getY() + e.getY()));
                        popup.setVisible(true);
                    }
                    return;
                }
                workSpace.setSelectedNote(null);
                if (e.getX() >= columnWidth - 10 && e.getX() <= columnWidth && e.getY() >= beginArrowY[2] && e.getY() <= beginArrowY[1] && !dragBegin) {
                    dragBeginLunch = false;
                    dragEndLunch = false;
                    dragBegin = true;
                    dragEnd = false;
                } else if (e.getX() >= columnWidth - 10 && e.getX() <= columnWidth && e.getY() >= endArrowY[2] && e.getY() <= endArrowY[1] && !dragEnd) {
                    dragBeginLunch = false;
                    dragEndLunch = false;
                    dragBegin = false;
                    dragEnd = true;
                } else if (e.getX() >= columnWidth - 10 && e.getX() <= columnWidth && e.getY() >= beginLunchArrowY[2] && e.getY() <= beginLunchArrowY[1] && !dragBeginLunch) {
                    dragBeginLunch = true;
                    dragEndLunch = false;
                    dragBegin = false;
                    dragEnd = false;
                } else if (e.getX() >= columnWidth - 10 && e.getX() <= columnWidth && e.getY() >= endLunchArrowY[2] && e.getY() <= endLunchArrowY[1] && !dragEndLunch) {
                    dragBeginLunch = false;
                    dragEndLunch = true;
                    dragBegin = false;
                    dragEnd = false;
                } else {
                    dragBeginLunch = false;
                    dragEndLunch = false;
                    dragBegin = false;
                    dragEnd = false;
                }
                if (sortedList != null) {
                    ListIterator iter = sortedList.listIterator(sortedList.size());
                    while (iter.hasPrevious()) {
                        Note testNote = (Note) iter.previous();
                        int i = testNote.getDate().get(Calendar.DAY_OF_YEAR) - workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) + ((workSpace.getCurrentDay().get(Calendar.DAY_OF_WEEK) + 5) % 7);
                        if (i < 7 && i >= 0) {
                            if (testNote.getNoteZone() != null && testNote.getCalendrier().isDisplay()) if (e.getX() >= testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 && e.getX() <= testNote.getNoteZone().getX() + columnWidth && e.getY() >= testNote.getNoteZone().getY() - 6 && e.getY() <= testNote.getNoteZone().getY() + testNote.getNoteZone().height + 6 && workSpace.getSelectedNote() == null) {
                                if (e.getClickCount() == 2) {
                                    testNote.setDragBottomEnable(false);
                                    testNote.setDragTopEnable(false);
                                    testNote.setDragEnable(false);
                                    notePanel.setVisible(true);
                                } else if (e.getClickCount() == 1 && !testNote.isDragEnable() && !testNote.isDragTopEnable() && !testNote.isDragBottomEnable()) {
                                    if (e.getY() >= testNote.getNoteZone().getY() - 5 && e.getY() <= testNote.getNoteZone().getY() + 5) {
                                        testNote.setDragTopEnable(!testNote.isDragTopEnable());
                                    } else if (e.getY() >= testNote.getNoteZone().getY() + testNote.getNoteZone().height - 5 && e.getY() <= testNote.getNoteZone().getY() + testNote.getNoteZone().height + 5) {
                                        testNote.setDragBottomEnable(!testNote.isDragBottomEnable());
                                    } else if (testNote.isSelected()) {
                                        testNote.setDragEnable(!testNote.isDragEnable());
                                    }
                                } else {
                                    testNote.setDragBottomEnable(false);
                                    testNote.setDragTopEnable(false);
                                    testNote.setDragEnable(false);
                                    testNote.setSelected(false);
                                }
                                testNote.setSelected(true);
                                workSpace.setSelectedNote(testNote);
                            } else {
                                testNote.setDragBottomEnable(false);
                                testNote.setDragTopEnable(false);
                                testNote.setDragEnable(false);
                                testNote.setSelected(false);
                            }
                        }
                    }
                }
                if (e.getClickCount() == 2 && workSpace.getSelectedNote() == null && workSpace.getCurrentCalendrier() != null) {
                    Note note3 = new Note();
                    note3.setCalendrier(workSpace.getCurrentCalendrier());
                    workSpace.getCurrentCalendrier().addNote(note3);
                    int xPos = (int) e.getX() / columnWidth;
                    int yPos = ((int) e.getY() - 20) / 40;
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(workSpace.getCurrentDay().getTime());
                    temp.add(Calendar.DAY_OF_YEAR, workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) - temp.get(Calendar.DAY_OF_YEAR));
                    temp.add(Calendar.DAY_OF_YEAR, -((temp.get(Calendar.DAY_OF_WEEK) + 5) % 7) + xPos - 1);
                    temp.set(Calendar.HOUR_OF_DAY, (int) (yPos));
                    temp.set(Calendar.MINUTE, ((int) ((e.getY() - 20) % 40) < 10 ? 0 : ((e.getY() - 20) % 40) < 20 ? 15 : ((e.getY() - 20) % 40) < 30 ? 30 : 45));
                    Calendar temp2 = Calendar.getInstance();
                    temp2.setTime(temp.getTime());
                    temp2.add(Calendar.HOUR_OF_DAY, 1);
                    note3.setDate(temp);
                    note3.setEndDate(temp2);
                    System.out.println("création note :" + temp.getTime().toString().substring(0, 10) + " : " + temp2.getTime().toString().substring(0, 10));
                    note3.setTitle("Nouvelle note");
                    note3.setSelected(true);
                    note3.setDragBottomEnable(true);
                    workSpace.setSelectedNote(note3);
                    noteList.add(note3);
                }
                repaint();
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public WeekGridPanel(int line, int column, JPanel agendaNotePanel, Workspace workSpace) {
        this(line, column, workSpace);
        this.notePanel = agendaNotePanel;
    }

    void createGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int i = pas; i < 1000; i += pas) {
            for (int j = columnWidth; j < getWidth() - 4; j = j + 3) if ((j - columnWidth) % 6 != 0) g.drawLine(j, i, j + 3, i);
        }
        for (int i = 25, j = 0; i < 1000; i += pas, j++) {
            g.drawLine(columnWidth, i - 5, getWidth() - 4, i - 5);
            g.drawString((j < 10 ? "0" : "") + j + ":00", 15, i);
        }
    }

    void paintNote(Note testNote, Graphics2D g2d) {
        int PosX = (((testNote.getDate().get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1) * columnWidth;
        int PosY = testNote.getDate().get(Calendar.HOUR_OF_DAY) * 40 + 20;
        if (testNote.getNoteZone() == null || testNote.getNoteZone().width != columnWidth) {
            if (testNote.getDate().get(Calendar.MINUTE) >= 45) PosY += 30; else if (testNote.getDate().get(Calendar.MINUTE) >= 30) PosY += 20; else if (testNote.getDate().get(Calendar.MINUTE) >= 15) PosY += 10;
            Calendar temp = Calendar.getInstance();
            temp.setTime(testNote.getEndDate().getTime());
            temp.add(Calendar.YEAR, -testNote.getDate().get(Calendar.YEAR));
            temp.add(Calendar.MONTH, -testNote.getDate().get(Calendar.MONTH));
            temp.add(Calendar.DAY_OF_MONTH, -testNote.getDate().get(Calendar.DAY_OF_MONTH));
            temp.add(Calendar.HOUR_OF_DAY, -testNote.getDate().get(Calendar.HOUR_OF_DAY));
            temp.add(Calendar.MINUTE, -testNote.getDate().get(Calendar.MINUTE));
            int longueur = temp.get(Calendar.HOUR_OF_DAY) * 40;
            if (temp.get(Calendar.MINUTE) >= 45) longueur += 30; else if (temp.get(Calendar.MINUTE) >= 30) longueur += 20; else if (temp.get(Calendar.MINUTE) >= 15) longueur += 10;
            testNote.setNoteZone(new RoundRectangle2D.Float(PosX, PosY, columnWidth, longueur - 1, 20, 20));
            testNote.setXPos(PosX / columnWidth);
            testNote.setYPos(20 + PosY / 40);
        }
        gradient = new GradientPaint((int) testNote.getNoteZone().getX(), (int) testNote.getNoteZone().getY(), testNote.getCalendrier().getColor(), (int) testNote.getNoteZone().getX() + (int) (testNote.getNoteZone().getWidth() * 1.4), (int) testNote.getNoteZone().getY(), Color.WHITE);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        clip1 = g2d.getClip();
        int effectiveWidth = columnWidth - (testNote.getConcurrentPos() * 15);
        g2d.clip(testNote.getNoteZone());
        g2d.setColor(testNote.getCalendrier().getColor());
        g2d.setClip(clip1);
        g2d.fillRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y), effectiveWidth, (int) testNote.getNoteZone().height, 18, 18);
        if (testNote.isSelected()) {
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y), effectiveWidth, (int) testNote.getNoteZone().height, 18, 18);
        }
        g2d.setPaint(p1);
        g2d.drawRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y), effectiveWidth, (int) testNote.getNoteZone().height - 1, 20, 20);
        if (testNote.isSelected()) g2d.setColor(Color.ORANGE); else g2d.setPaint(p2);
        g2d.drawRoundRect(1 + testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y) + 1, effectiveWidth, (int) testNote.getNoteZone().height - 3, 18, 18);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        Rectangle rectClipBottom = new Rectangle(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y + testNote.getNoteZone().getHeight()) - 20, effectiveWidth, 20);
        g2d.clip(rectClipBottom);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15 + 1, (int) (testNote.getNoteZone().y), effectiveWidth - 2, (int) testNote.getNoteZone().getHeight() - 1, 18, 18);
        if (testNote.isSelected()) {
            g2d.setColor(Color.WHITE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.fillRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15 + 1, (int) (testNote.getNoteZone().y), effectiveWidth - 2, (int) testNote.getNoteZone().getHeight() - 1, 18, 18);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        }
        if (testNote.isSelected()) g2d.setColor(Color.DARK_GRAY); else g2d.setColor(Color.WHITE);
        g2d.setFont(new java.awt.Font("Comic Sans MS", 1, 9));
        g2d.drawString(testNote.getDate().getTime().toString().substring(11, 16) + "->" + testNote.getEndDate().getTime().toString().substring(11, 16), testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15 + 5, (int) (testNote.getNoteZone().y + testNote.getNoteZone().getHeight()) - 7);
        g2d.setClip(clip1);
        g2d.setPaint(gradient);
        Rectangle rectClip = new Rectangle(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y), effectiveWidth, 20);
        g2d.clip(rectClip);
        g2d.fillRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y), effectiveWidth, (int) testNote.getNoteZone().height, 18, 18);
        if (testNote.isSelected()) {
            g2d.setColor(Color.WHITE);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.fillRoundRect(testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15, (int) (testNote.getNoteZone().y), effectiveWidth, (int) testNote.getNoteZone().height, 18, 18);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        }
        if (testNote.isSelected()) g2d.setColor(Color.DARK_GRAY); else g2d.setColor(Color.WHITE);
        g2d.setFont(new java.awt.Font("Comic Sans MS", 1, 12));
        g2d.drawString(testNote.getTitle(), testNote.getXPos() * columnWidth + testNote.getConcurrentPos() * 15 + 5, (int) (testNote.getNoteZone().y) + 13);
        g2d.setClip(clip1);
        if (testNote.isSelected()) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            if (testNote.isDragBottomEnable()) {
                int arrowPosY = (int) (testNote.getNoteZone().getY() + testNote.getNoteZone().getHeight());
                int arrowPosX = (int) (testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 + effectiveWidth / 2.0);
                resizeArrowX[0] = arrowPosX - 7;
                resizeArrowX[1] = arrowPosX;
                resizeArrowX[2] = arrowPosX + 7;
                resizeArrowY[0] = arrowPosY;
                resizeArrowY[1] = arrowPosY + 7;
                resizeArrowY[2] = arrowPosY;
                g2d.fillPolygon(resizeArrowX, resizeArrowY, 3);
            } else if (testNote.isDragTopEnable()) {
                int arrowPosY = (int) (testNote.getNoteZone().getY()) + 1;
                int arrowPosX = (int) (testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 + effectiveWidth / 2.0);
                resizeArrowX[0] = arrowPosX - 7;
                resizeArrowX[1] = arrowPosX;
                resizeArrowX[2] = arrowPosX + 7;
                resizeArrowY[0] = arrowPosY;
                resizeArrowY[1] = arrowPosY - 7;
                resizeArrowY[2] = arrowPosY;
                g2d.fillPolygon(resizeArrowX, resizeArrowY, 3);
            } else if (testNote.isDragEnable()) {
                int arrowPosY = (int) (testNote.getNoteZone().getY());
                int arrowPosX = (int) (testNote.getNoteZone().getX() + testNote.getConcurrentPos() * 15 + effectiveWidth / 2.);
                resizeArrowX[0] = arrowPosX - 7;
                resizeArrowX[1] = arrowPosX;
                resizeArrowX[2] = arrowPosX + 7;
                resizeArrowY[0] = arrowPosY;
                resizeArrowY[1] = arrowPosY - 7;
                resizeArrowY[2] = arrowPosY;
                g2d.fillPolygon(resizeArrowX, resizeArrowY, 3);
                arrowPosY = (int) (testNote.getNoteZone().getY() + testNote.getNoteZone().getHeight());
                resizeArrowY[0] = arrowPosY;
                resizeArrowY[1] = arrowPosY + 7;
                resizeArrowY[2] = arrowPosY;
                arrowPosY = (int) (testNote.getNoteZone().getY() + testNote.getNoteZone().getHeight() / 2);
                g2d.fillPolygon(resizeArrowX, resizeArrowY, 3);
                resizeArrowY[0] = arrowPosY - 7;
                resizeArrowY[1] = arrowPosY;
                resizeArrowY[2] = arrowPosY + 7;
                resizeArrowX[0] = arrowPosX - effectiveWidth / 2 + 1;
                resizeArrowX[1] = arrowPosX - effectiveWidth / 2 - 7 + 1;
                resizeArrowX[2] = arrowPosX - effectiveWidth / 2 + 1;
                g2d.fillPolygon(resizeArrowX, resizeArrowY, 3);
                resizeArrowX[0] = arrowPosX + effectiveWidth / 2 + 1;
                resizeArrowX[1] = arrowPosX + effectiveWidth / 2 + 7 + 1;
                resizeArrowX[2] = arrowPosX + effectiveWidth / 2 + 1;
                g2d.fillPolygon(resizeArrowX, resizeArrowY, 3);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        noteList = workSpace.getNoteList();
        columnWidth = getWidth() / column;
        ArrowX[0] = columnWidth;
        ArrowX[1] = columnWidth - 10;
        ArrowX[2] = columnWidth - 10;
        g2d = (Graphics2D) g.create();
        r2d1 = new RoundRectangle2D.Float(columnWidth, 0, getWidth() - columnWidth - 5, beginArrowY[0], 20, 20);
        r2d2 = new RoundRectangle2D.Float(columnWidth, endArrowY[0], getWidth() - columnWidth - 5, getHeight() - 20 - endArrowY[0], 20, 20);
        r2d3 = new RoundRectangle2D.Float(columnWidth, beginLunchArrowY[0], getWidth() - columnWidth - 5, endLunchArrowY[0] - beginLunchArrowY[0], 20, 20);
        clip1 = g2d.getClip();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        Calendar temp1 = Calendar.getInstance();
        temp1.setTime(workSpace.getCurrentDay().getTime());
        Calendar temp3 = Calendar.getInstance();
        temp3.add(Calendar.DAY_OF_YEAR, workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) - temp3.get(Calendar.DAY_OF_YEAR));
        temp3.add(Calendar.DAY_OF_YEAR, -((temp3.get(Calendar.DAY_OF_WEEK) + 5) % 7));
        int k = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - temp3.get(Calendar.DAY_OF_YEAR);
        if (k < 7 && k >= 0) {
            System.out.println(" AUJOURD'HUI");
            r2d4 = new RoundRectangle2D.Float(columnWidth + columnWidth * (((temp3.get(Calendar.DAY_OF_WEEK) + 5 + k) % 7)), 0, columnWidth, getHeight() - 20, 20, 20);
            g2d.clip(r2d4);
            g2d.setColor(new Color(220, 220, 255));
            g2d.fillRoundRect(columnWidth + columnWidth * (((temp3.get(Calendar.DAY_OF_WEEK) + 5 + k) % 7)), 0, columnWidth - 2, getHeight() - 20, 20, 20);
            g2d.setClip(clip1);
            g2d.setColor(new Color(150, 150, 255));
            g2d.drawRoundRect(columnWidth + columnWidth * (((temp3.get(Calendar.DAY_OF_WEEK) + 5 + k) % 7)), 0, columnWidth - 1, getHeight() - 20, 20, 20);
            g2d.setColor(new Color(200, 200, 255));
            g2d.drawRoundRect(columnWidth + columnWidth * (((temp3.get(Calendar.DAY_OF_WEEK) + 5 + k) % 7)), 0, columnWidth - 2, getHeight() - 21, 18, 18);
        }
        createGrid(g);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        updateConcurrent();
        Calendar temp = Calendar.getInstance();
        temp.setTime(workSpace.getCurrentDay().getTime());
        if (sortedList != null) for (Note testNote : sortedList) {
            temp.add(Calendar.DAY_OF_YEAR, workSpace.getCurrentDay().get(Calendar.DAY_OF_YEAR) - temp.get(Calendar.DAY_OF_YEAR));
            temp.add(Calendar.DAY_OF_YEAR, -((temp.get(Calendar.DAY_OF_WEEK) + 5) % 7));
            int j = testNote.getDate().get(Calendar.DAY_OF_YEAR) - temp.get(Calendar.DAY_OF_YEAR);
            if (j < 7 && j >= 0 && testNote.getCalendrier().isDisplay()) paintNote(testNote, g2d);
        }
        if (workSpace.getSelectedNote() != null) {
            int j = workSpace.getSelectedNote().getDate().get(Calendar.DAY_OF_YEAR) - temp.get(Calendar.DAY_OF_YEAR);
            if (j < 7 && j >= 0 && workSpace.getSelectedNote().getCalendrier().isDisplay()) paintNote(workSpace.getSelectedNote(), g2d);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.clip(r2d1);
        g2d.setPaint(p2);
        g2d.fillRect(columnWidth, 0, getWidth() - 4 - columnWidth, beginArrowY[0]);
        g2d.setClip(clip1);
        g2d.setPaint(p1);
        g2d.drawRoundRect(columnWidth, 0, getWidth() - 5 - columnWidth, beginArrowY[0], 20, 20);
        g2d.setPaint(p2);
        g2d.drawRoundRect(columnWidth + 1, 1, getWidth() - 7 - columnWidth, beginArrowY[0] - 3, 18, 18);
        if (dragBegin) g2d.setColor(Color.RED); else g2d.setColor(Color.BLACK);
        g2d.fillPolygon(ArrowX, beginArrowY, 3);
        g2d.clip(r2d2);
        g2d.setPaint(p2);
        g2d.fillRect(columnWidth, endArrowY[0], getWidth() - columnWidth - 5, getHeight() - 20 - endArrowY[0]);
        g2d.setClip(clip1);
        g2d.setPaint(p1);
        g2d.drawRoundRect(columnWidth, endArrowY[0], getWidth() - columnWidth - 5, getHeight() - 20 - endArrowY[0], 20, 20);
        g2d.setPaint(p2);
        g2d.drawRoundRect(columnWidth + 1, endArrowY[0] + 1, getWidth() - columnWidth - 7, getHeight() - 20 - endArrowY[0] - 3, 18, 18);
        if (dragEnd) g2d.setColor(Color.RED); else g2d.setColor(Color.BLACK);
        g2d.fillPolygon(ArrowX, endArrowY, 3);
        g2d.clip(r2d3);
        g2d.setPaint(p2);
        g2d.fillRect(columnWidth, beginLunchArrowY[0], getWidth() - 5 - columnWidth, endLunchArrowY[0] - beginLunchArrowY[0]);
        g2d.setClip(clip1);
        g2d.setPaint(p1);
        g2d.drawRoundRect(columnWidth, beginLunchArrowY[0], getWidth() - 5 - columnWidth, endLunchArrowY[0] - beginLunchArrowY[0], 20, 20);
        g2d.setPaint(p2);
        g2d.drawRoundRect(columnWidth + 1, beginLunchArrowY[0] + 1, getWidth() - 7 - columnWidth, endLunchArrowY[0] - beginLunchArrowY[0] - 3, 18, 18);
        if (dragBeginLunch) g2d.setColor(Color.RED); else g2d.setColor(Color.BLACK);
        g2d.fillPolygon(ArrowX, beginLunchArrowY, 3);
        if (dragEndLunch) g2d.setColor(Color.RED); else g2d.setColor(Color.BLACK);
        g2d.fillPolygon(ArrowX, endLunchArrowY, 3);
        g2d.dispose();
    }

    int getConcurentCout(Note note) {
        ListIterator iter = sortedList.listIterator(sortedList.indexOf(note));
        while (iter.hasPrevious()) {
            Note temp = (Note) iter.previous();
            if (note.getNoteZone() != null && temp.getNoteZone() != null && note.getCalendrier().isDisplay() && temp.getCalendrier().isDisplay()) if (note.getDate().get(Calendar.DAY_OF_YEAR) == temp.getDate().get(Calendar.DAY_OF_YEAR)) if ((note.getNoteZone().getY() >= temp.getNoteZone().getY() && note.getNoteZone().getY() < temp.getNoteZone().getY() + temp.getNoteZone().getHeight()) || (temp.getNoteZone().getY() >= note.getNoteZone().getY() && temp.getNoteZone().getY() < note.getNoteZone().getY() + note.getNoteZone().getHeight()) && (note != temp)) {
                return temp.getConcurrentPos() + 1;
            }
        }
        return 0;
    }

    void updateConcurrent() {
        if (noteList.size() > 0) {
            sortedList = new LinkedList<Note>();
            sortedList.add(noteList.element());
            noteList.element().setConcurrent(0);
            noteList.element().setConcurrentPos(0);
            for (Note testNote1 : noteList.subList(1, noteList.size())) {
                testNote1.setConcurrent(0);
                testNote1.setConcurrentPos(0);
                ListIterator iter = sortedList.listIterator();
                while (iter.hasNext() && testNote1.getNoteZone() != null && sortedList.get(iter.nextIndex()).getNoteZone() != null && sortedList.get(iter.nextIndex()).getNoteZone().getHeight() > testNote1.getNoteZone().getHeight()) {
                    iter.next();
                }
                if (testNote1.getCalendrier() != null) if (testNote1.getCalendrier().isDisplay()) iter.add(testNote1);
            }
            for (Note testNote1 : sortedList) {
                testNote1.setConcurrentPos(getConcurentCout(testNote1));
            }
        }
    }
}
