package org.ximtec.igesture.io.tuio.tuio2D.handler;

import java.util.Enumeration;
import java.util.Vector;
import org.ximtec.igesture.io.tuio.TuioListener;
import org.ximtec.igesture.io.tuio.TuioTime;
import org.ximtec.igesture.io.tuio.handler.AbstractTuioCursorHandler;
import org.ximtec.igesture.io.tuio.tuio2D.TuioCursor;
import com.illposed.osc.OSCMessage;

/**
 * Handles /tuio/2Dcur messages.
 * @author Bjorn Puype
 *
 */
public class TuioCursorHandler extends AbstractTuioCursorHandler<TuioCursor> {

    /** Constructor */
    public TuioCursorHandler() {
    }

    @Override
    public void acceptMessage(OSCMessage message, TuioTime currentTime) {
        Object[] args = message.getArguments();
        String command = (String) args[0];
        if (command.equals("set")) {
            long s_id = ((Integer) args[1]).longValue();
            float xpos = ((Float) args[2]).floatValue();
            float ypos = ((Float) args[3]).floatValue();
            float xspeed = ((Float) args[4]).floatValue();
            float yspeed = ((Float) args[5]).floatValue();
            float maccel = ((Float) args[6]).floatValue();
            if (cursorList.get(s_id) == null) {
                TuioCursor addCursor = new TuioCursor(s_id, -1, xpos, ypos);
                frameCursors.addElement(addCursor);
            } else {
                TuioCursor tcur = cursorList.get(s_id);
                if (tcur == null) return;
                if ((tcur.getX() != xpos) || (tcur.getY() != ypos) || (tcur.getXSpeed() != xspeed) || (tcur.getYSpeed() != yspeed) || (tcur.getMotionAccel() != maccel)) {
                    TuioCursor updateCursor = new TuioCursor(s_id, tcur.getCursorID(), xpos, ypos);
                    updateCursor.update(xpos, ypos, xspeed, yspeed, maccel);
                    frameCursors.addElement(updateCursor);
                }
            }
        } else if (command.equals("alive")) {
            newCursorList.clear();
            for (int i = 1; i < args.length; i++) {
                long s_id = ((Integer) args[i]).longValue();
                newCursorList.addElement(s_id);
                if (aliveCursorList.contains(s_id)) aliveCursorList.removeElement(s_id);
            }
            for (int i = 0; i < aliveCursorList.size(); i++) {
                TuioCursor removeCursor = cursorList.get(aliveCursorList.elementAt(i));
                if (removeCursor == null) continue;
                removeCursor.remove(currentTime);
                frameCursors.addElement(removeCursor);
            }
        } else if (command.equals("fseq")) {
            long fseq = ((Integer) args[1]).longValue();
            boolean lateFrame = false;
            if (fseq > 0) {
                if (fseq > currentFrame) currentTime = TuioTime.getSessionTime();
                if ((fseq >= currentFrame) || ((currentFrame - fseq) > 100)) currentFrame = fseq; else lateFrame = true;
            } else if (TuioTime.getSessionTime().subtract(currentTime).getTotalMilliseconds() > 100) {
                currentTime = TuioTime.getSessionTime();
            }
            if (!lateFrame) {
                Enumeration<TuioCursor> frameEnum = frameCursors.elements();
                while (frameEnum.hasMoreElements()) {
                    TuioCursor tcur = frameEnum.nextElement();
                    switch(tcur.getTuioState()) {
                        case TuioCursor.TUIO_REMOVED:
                            TuioCursor removeCursor = tcur;
                            removeCursor.remove(currentTime);
                            for (int i = 0; i < listenerList.size(); i++) {
                                TuioListener listener = (TuioListener) listenerList.elementAt(i);
                                if (listener != null) listener.removeTuioCursor(removeCursor);
                            }
                            cursorList.remove(removeCursor.getSessionID());
                            if (removeCursor.getCursorID() == maxCursorID) {
                                maxCursorID = -1;
                                if (cursorList.size() > 0) {
                                    Enumeration<TuioCursor> clist = cursorList.elements();
                                    while (clist.hasMoreElements()) {
                                        int c_id = clist.nextElement().getCursorID();
                                        if (c_id > maxCursorID) maxCursorID = c_id;
                                    }
                                    Enumeration<TuioCursor> flist = freeCursorList.elements();
                                    while (flist.hasMoreElements()) {
                                        int c_id = flist.nextElement().getCursorID();
                                        if (c_id >= maxCursorID) freeCursorList.removeElement(c_id);
                                    }
                                } else freeCursorList.clear();
                            } else if (removeCursor.getCursorID() < maxCursorID) {
                                freeCursorList.addElement(removeCursor);
                            }
                            break;
                        case TuioCursor.TUIO_ADDED:
                            int c_id = cursorList.size();
                            if ((cursorList.size() <= maxCursorID) && (freeCursorList.size() > 0)) {
                                TuioCursor closestCursor = freeCursorList.firstElement();
                                Enumeration<TuioCursor> testList = freeCursorList.elements();
                                while (testList.hasMoreElements()) {
                                    TuioCursor testCursor = testList.nextElement();
                                    if (testCursor.getDistance(tcur) < closestCursor.getDistance(tcur)) closestCursor = testCursor;
                                }
                                c_id = closestCursor.getCursorID();
                                freeCursorList.removeElement(closestCursor);
                            } else maxCursorID = c_id;
                            TuioCursor addCursor = new TuioCursor(currentTime, tcur.getSessionID(), c_id, tcur.getX(), tcur.getY());
                            cursorList.put(addCursor.getSessionID(), addCursor);
                            for (int i = 0; i < listenerList.size(); i++) {
                                TuioListener listener = (TuioListener) listenerList.elementAt(i);
                                if (listener != null) listener.addTuioCursor(addCursor);
                            }
                            break;
                        default:
                            TuioCursor updateCursor = cursorList.get(tcur.getSessionID());
                            if ((tcur.getX() != updateCursor.getX() && tcur.getXSpeed() == 0) || (tcur.getY() != updateCursor.getY() && tcur.getYSpeed() == 0)) updateCursor.update(currentTime, tcur.getX(), tcur.getY()); else updateCursor.update(currentTime, tcur.getX(), tcur.getY(), tcur.getXSpeed(), tcur.getYSpeed(), tcur.getMotionAccel());
                            for (int i = 0; i < listenerList.size(); i++) {
                                TuioListener listener = (TuioListener) listenerList.elementAt(i);
                                if (listener != null) listener.updateTuioCursor(updateCursor);
                            }
                    }
                }
                for (int i = 0; i < listenerList.size(); i++) {
                    TuioListener listener = (TuioListener) listenerList.elementAt(i);
                    if (listener != null) listener.refresh(new TuioTime(currentTime));
                }
                Vector<Long> buffer = aliveCursorList;
                aliveCursorList = newCursorList;
                newCursorList = buffer;
            }
            frameCursors.clear();
        }
    }
}
