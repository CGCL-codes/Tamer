package net.sf.isnake.ai.viper.core;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import net.sf.isnake.ai.viper.ui.AiDebugger;
import net.sf.isnake.core.GameFieldMatrix;

/**The implementation of the slicing algorithm.
 *
 * @author suraj
 * @version $Id$
 */
public class GameFieldSlicer implements Runnable {

    private GameFieldMatrix gameFieldMatrix;

    private FundamentalOpenRectangle For;

    private int i, j, id;

    private Set<FundamentalOpenRectangle> FOR;

    private AiDebugger ui;

    private boolean sliced = false;

    public GameFieldSlicer(GameFieldMatrix gameFieldMatrix, AiDebugger ui) {
        this.gameFieldMatrix = gameFieldMatrix;
        this.ui = ui;
        this.FOR = new TreeSet<FundamentalOpenRectangle>();
        this.id = 0;
    }

    public void run() {
        sliced = false;
        this.FOR.clear();
        this.id = 0;
        ui.startTimer(AiDebugger.ANALYSE);
        if (this.isNoWall()) {
            getFOR().add(new FundamentalOpenRectangle(0, 0, (gameFieldMatrix.GAME_FIELD_WIDTH / 2) - 1, (gameFieldMatrix.GAME_FIELD_HEIGHT / 2) - 1, ++id));
            getFOR().add(new FundamentalOpenRectangle(0, (gameFieldMatrix.GAME_FIELD_WIDTH / 2), (gameFieldMatrix.GAME_FIELD_WIDTH / 2) - 1, (gameFieldMatrix.GAME_FIELD_HEIGHT - 1), ++id));
            getFOR().add(new FundamentalOpenRectangle((gameFieldMatrix.GAME_FIELD_WIDTH / 2), (gameFieldMatrix.GAME_FIELD_HEIGHT / 2), (gameFieldMatrix.GAME_FIELD_WIDTH - 1), (gameFieldMatrix.GAME_FIELD_HEIGHT - 1), ++id));
            getFOR().add(new FundamentalOpenRectangle((gameFieldMatrix.GAME_FIELD_WIDTH / 2), 0, (gameFieldMatrix.GAME_FIELD_WIDTH - 1), (gameFieldMatrix.GAME_FIELD_HEIGHT / 2) - 1, ++id));
        } else sliceFromLeftRight(0, gameFieldMatrix.GAME_FIELD_WIDTH, 0, gameFieldMatrix.GAME_FIELD_HEIGHT);
        ui.stopTimer(AiDebugger.ANALYSE);
        ui.log("Slicing Complete!!!");
        ui.startTimer(AiDebugger.OPTIMISE);
        optimiseFOR();
        ui.stopTimer(AiDebugger.OPTIMISE);
        ui.log("Optimization Complete!!!");
        ui.startTimer(AiDebugger.MAP);
        ui.getGameFieldMapper().map();
        ui.stopTimer(AiDebugger.MAP);
        ui.log("Mapping Complete!!!");
        ui.rePaintGameField();
        sliced = false;
    }

    private boolean isNoWall() {
        for (i = 0; i < gameFieldMatrix.GAME_FIELD_WIDTH; i++) for (j = 0; j < gameFieldMatrix.GAME_FIELD_HEIGHT; j++) if (this.gameFieldMatrix.getGameFieldObject(i, j).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) return false;
        return true;
    }

    public void sliceFromLeftRight(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From left and right");
        int right, left;
        doneR: {
            for (i = lt_lft; i < lt_rgt; i++) {
                for (j = lt_top; j < lt_bot; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(i, j).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) break doneR;
                }
            }
        }
        right = i;
        doneL: {
            for (i = lt_rgt; i > lt_lft; i--) {
                for (j = lt_top; j < lt_bot; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(i - 1, j) == GameFieldMatrix.OBJECT_WALL) break doneL;
                }
            }
        }
        left = i;
        if (right == lt_lft && left == lt_rgt) {
            sliceFromBottomTop(lt_lft, lt_rgt, lt_top, lt_bot);
        } else if (right == lt_lft && left < lt_rgt) {
            getFOR().add(new FundamentalOpenRectangle(left, lt_top, lt_rgt - 1, lt_bot - 1, ++id));
            sliceFromBottomTop(lt_lft, left, lt_top, lt_bot);
        } else if (right > lt_lft && left == lt_rgt) {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, right - 1, lt_bot - 1, ++id));
            sliceFromBottomTop(right, lt_rgt, lt_top, lt_bot);
        } else {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, right - 1, lt_bot - 1, ++id));
            getFOR().add(new FundamentalOpenRectangle(left, lt_top, lt_rgt - 1, lt_bot - 1, ++id));
            sliceFromBottomTop(right, left, lt_top, lt_bot);
        }
    }

    public void sliceFromBottomTop(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From bottom and top");
        int bottom, top;
        doneB: {
            for (i = lt_bot; i > lt_top; i--) {
                for (j = lt_lft; j < lt_rgt; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(j, i - 1).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) break doneB;
                }
            }
        }
        top = i;
        doneT: {
            for (i = lt_top; i < lt_bot; i++) {
                for (j = lt_lft; j < lt_rgt; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(j, i) == GameFieldMatrix.OBJECT_WALL) break doneT;
                }
            }
        }
        bottom = i;
        if (top == lt_bot && bottom == lt_top) {
            sliceFromTopLeft(lt_lft, lt_rgt, lt_top, lt_bot);
        } else if (top == lt_bot && bottom > lt_top) {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, lt_rgt - 1, bottom - 1, ++id));
            sliceFromTopLeft(lt_lft, lt_rgt, bottom, lt_bot);
        } else if (top < lt_bot && bottom == lt_top) {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, top, lt_rgt - 1, lt_bot - 1, ++id));
            sliceFromTopLeft(lt_lft, lt_rgt, lt_top, top);
        } else {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, lt_rgt - 1, bottom - 1, ++id));
            getFOR().add(new FundamentalOpenRectangle(lt_lft, top, lt_rgt - 1, lt_bot - 1, ++id));
            sliceFromTopLeft(lt_lft, lt_rgt, bottom, top);
        }
    }

    public void sliceFromLeft(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From left");
        if (isFullBlocked(lt_lft, lt_rgt, lt_top, lt_bot)) return;
        if (lt_lft == lt_rgt) return;
        done: {
            for (i = lt_lft; i < lt_rgt; i++) {
                for (j = lt_top; j < lt_bot; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(i, j).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) break done;
                }
            }
        }
        if (lt_lft < i) getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, i - 1, lt_bot - 1, ++id));
        lt_lft = i;
        sliceFromBottom(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromBottom(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From bottom");
        if (lt_top == lt_bot) return;
        done: {
            for (i = lt_bot; i > lt_top; i--) {
                for (j = lt_lft; j < lt_rgt; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(j, i - 1).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) break done;
                }
            }
        }
        if (lt_bot > i) getFOR().add(new FundamentalOpenRectangle(lt_lft, i, lt_rgt - 1, lt_bot - 1, ++id));
        lt_bot = i;
        sliceFromRight(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromRight(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From right");
        if (lt_lft == lt_rgt) return;
        done: {
            for (i = lt_rgt; i > lt_lft; i--) {
                for (j = lt_top; j < lt_bot; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(i - 1, j).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) break done;
                }
            }
        }
        if (lt_rgt > i) getFOR().add(new FundamentalOpenRectangle(i, lt_top, lt_rgt - 1, lt_bot - 1, ++id));
        lt_rgt = i;
        sliceFromTop(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromTop(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From top");
        if (lt_top == lt_bot) return;
        done: {
            for (i = lt_top; i < lt_bot; i++) {
                for (j = lt_lft; j < lt_rgt; j++) {
                    if (this.gameFieldMatrix.getGameFieldObject(j, i).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) break done;
                }
            }
        }
        if (lt_top < i) getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, lt_rgt - 1, i - 1, ++id));
        lt_top = i;
        sliceFromTopLeft(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromTopLeft(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From top-left");
        int right = lt_rgt, bottom = lt_bot, r = 0, b = 0, a = 0, a1 = 0;
        for (i = lt_lft; i < right; i++) {
            for (j = lt_top; j < bottom; j++) {
                if (this.gameFieldMatrix.getGameFieldObject(i, j).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) {
                    if (i > lt_lft) {
                        a1 = (i - lt_lft) * (bottom - lt_top);
                        if (a1 > a) {
                            a = a1;
                            r = i;
                            b = bottom;
                        }
                    }
                    bottom = j;
                    if (bottom == lt_top) right = i;
                }
            }
        }
        if (right == lt_rgt && i > lt_lft) {
            a1 = (lt_rgt - lt_lft) * (bottom - lt_top);
            if (a1 > a) {
                a = a1;
                r = lt_rgt;
                b = bottom;
            }
        }
        if (a != 0) {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, lt_top, r - 1, b - 1, ++id));
            sliceFromLeft(r, lt_rgt, lt_top, b);
            sliceFromLeft(lt_lft, lt_rgt, b, lt_bot);
        } else sliceFromBottomLeft(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromBottomLeft(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From bottom-left");
        int right = lt_rgt, top = lt_top, r = 0, t = 0, a = 0, a1 = 0;
        for (i = lt_lft; i < right; i++) {
            for (j = lt_bot; j > top; j--) {
                if (this.gameFieldMatrix.getGameFieldObject(i, j - 1).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) {
                    if (i > lt_lft) {
                        a1 = (i - lt_lft) * (lt_bot - top);
                        if (a1 > a) {
                            a = a1;
                            r = i;
                            t = top;
                        }
                    }
                    top = j;
                    if (top == lt_bot) right = i;
                }
            }
        }
        if (right == lt_rgt && i > lt_lft) {
            a1 = (i - lt_lft) * (lt_bot - top);
            if (a1 > a) {
                a = a1;
                r = i;
                t = top;
            }
        }
        if (a != 0) {
            getFOR().add(new FundamentalOpenRectangle(lt_lft, t, r - 1, lt_bot - 1, ++id));
            sliceFromLeft(r, lt_rgt, t, lt_bot);
            sliceFromLeft(lt_lft, lt_rgt, lt_top, t);
        } else sliceFromBottomRight(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromBottomRight(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From bottom-right");
        int left = lt_lft, top = lt_top, l = 0, t = 0, a = 0, a1 = 0;
        for (i = lt_rgt; i > left; i--) {
            for (j = lt_bot; j > top; j--) {
                if (this.gameFieldMatrix.getGameFieldObject(i - 1, j - 1).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) {
                    if (i < lt_rgt) {
                        a1 = (lt_rgt - i) * (lt_bot - top);
                        if (a1 > a) {
                            a = a1;
                            l = i;
                            t = top;
                        }
                    }
                    top = j;
                    if (top == lt_bot) left = i;
                }
            }
        }
        if (left == lt_lft && i < lt_rgt) {
            a1 = (lt_rgt - i) * (lt_bot - top);
            if (a1 > a) {
                a = a1;
                l = i;
                t = top;
            }
        }
        if (a != 0) {
            getFOR().add(new FundamentalOpenRectangle(l, t, lt_rgt - 1, lt_bot - 1, ++id));
            sliceFromLeft(lt_lft, l, t, lt_bot);
            sliceFromLeft(lt_lft, lt_rgt, lt_top, t);
        } else sliceFromTopRight(lt_lft, lt_rgt, lt_top, lt_bot);
    }

    public void sliceFromTopRight(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        ui.log("Slicing From top right" + id);
        int left = lt_lft, bottom = lt_bot, l = 0, b = 0, a = 0, a1 = 0;
        for (i = lt_rgt; i > left; i--) {
            for (j = lt_top; j < bottom; j++) {
                if (this.gameFieldMatrix.getGameFieldObject(i - 1, j).intValue() == GameFieldMatrix.OBJECT_WALL.intValue()) {
                    if (i < lt_rgt) {
                        a1 = (i - lt_rgt) * (lt_top - bottom);
                        if (a1 > a) {
                            a = a1;
                            l = i;
                            b = bottom;
                        }
                    }
                    bottom = j;
                    if (bottom == lt_top) left = i;
                }
            }
        }
        if (left == lt_lft && i < lt_rgt) {
            a1 = (i - lt_rgt) * (lt_top - bottom);
            if (a1 > a) {
                a = a1;
                l = i;
                b = bottom;
            }
        }
        if (a != 0) {
            getFOR().add(new FundamentalOpenRectangle(l, lt_top, lt_rgt - 1, b - 1, ++id));
            sliceFromLeft(lt_lft, l, lt_top, b);
            sliceFromLeft(lt_lft, lt_rgt, b, lt_bot);
        } else {
            if (lt_lft + 1 == lt_rgt && lt_top + 1 == lt_bot) return;
            if (lt_lft + 1 == lt_rgt) {
                sliceFromTop(lt_lft, lt_rgt, lt_top + 1, lt_bot);
            } else if (lt_top + 1 == lt_bot) {
                sliceFromLeft(lt_lft + 1, lt_rgt, lt_top, lt_bot);
            } else {
                sliceFromLeft(lt_lft + 1, lt_rgt, lt_top, lt_top + 1);
                sliceFromTop(lt_lft, lt_lft + 1, lt_top + 1, lt_bot);
                sliceFromTopLeft(lt_lft + 1, lt_rgt, lt_top + 1, lt_bot);
            }
        }
    }

    private boolean isFullBlocked(int lt_lft, int lt_rgt, int lt_top, int lt_bot) {
        for (int i = lt_lft; i < lt_rgt; i++) {
            for (int j = lt_top; j < lt_bot; j++) {
                if (this.gameFieldMatrix.getGameFieldObject(i, j).intValue() != GameFieldMatrix.OBJECT_WALL.intValue()) return false;
            }
        }
        return true;
    }

    /**Combine the FOR's if possible
     */
    public void optimiseFOR() {
        Iterator iter1 = this.getFOR().iterator();
        FundamentalOpenRectangle For1, For2, ret;
        while (iter1.hasNext()) {
            For1 = (FundamentalOpenRectangle) iter1.next();
            Iterator iter2 = this.getFOR().iterator();
            while (iter2.hasNext()) {
                For2 = (FundamentalOpenRectangle) iter2.next();
                if (!For1.equals(For2)) {
                    ret = CombineIfPossible(For1, For2);
                    if (ret != null) {
                        ui.log("Combining " + For1.getId() + " and " + For2.getId() + " ==> " + For1.getId());
                        this.getFOR().remove(For1);
                        this.getFOR().remove(For2);
                        this.getFOR().add(ret);
                        this.optimiseFOR();
                        return;
                    }
                }
            }
        }
        iter1 = this.getFOR().iterator();
        FundamentalOpenRectangle fa, fb;
        while (iter1.hasNext()) {
            For1 = (FundamentalOpenRectangle) iter1.next();
            if ((For1.getRight() - For1.getLeft()) == 57) {
                fa = new FundamentalOpenRectangle(For1.getLeft(), For1.getTop(), 29, For1.getBottom(), ++id);
                fb = new FundamentalOpenRectangle(30, For1.getTop(), For1.getRight(), For1.getBottom(), ++id);
                this.getFOR().remove(For1);
                this.getFOR().add(fa);
                this.getFOR().add(fb);
                iter1 = this.getFOR().iterator();
            } else if ((For1.getBottom() - For1.getTop()) == 57) {
                fa = new FundamentalOpenRectangle(For1.getLeft(), For1.getTop(), For1.getRight(), 29, ++id);
                fb = new FundamentalOpenRectangle(For1.getLeft(), 30, For1.getRight(), For1.getBottom(), ++id);
                this.getFOR().remove(For1);
                this.getFOR().add(fa);
                this.getFOR().add(fb);
                iter1 = this.getFOR().iterator();
            }
        }
        iter1 = this.getFOR().iterator();
        while (iter1.hasNext()) {
            ((FundamentalOpenRectangle) iter1.next()).setId(++id);
        }
        iter1 = this.getFOR().iterator();
        id = 0;
        while (iter1.hasNext()) {
            ((FundamentalOpenRectangle) iter1.next()).setId(++id);
        }
        ui.log("Optimiation Complete!!!");
    }

    /**Returns the combined rectangle if the combination is possible else returns <em>null</em>
     * @param for1 One of the two FOR required to combine
     * @param for2 The next one
     */
    private FundamentalOpenRectangle CombineIfPossible(FundamentalOpenRectangle for1, FundamentalOpenRectangle for2) {
        if ((for1.getRight() + 1) == for2.getLeft() && for1.getTop() == for2.getTop() && for1.getBottom() == for2.getBottom()) {
            return new FundamentalOpenRectangle(for1.getLeft(), for1.getTop(), for2.getRight(), for2.getBottom(), for1.getId());
        } else if (for1.getLeft() == (for2.getRight() + 1) && for1.getTop() == for2.getTop() && for1.getBottom() == for2.getBottom()) {
            return new FundamentalOpenRectangle(for2.getLeft(), for2.getTop(), for1.getRight(), for1.getBottom(), for1.getId());
        } else if ((for1.getBottom() + 1) == for2.getTop() && for1.getLeft() == for2.getLeft() && for1.getRight() == for2.getRight()) {
            return new FundamentalOpenRectangle(for1.getLeft(), for1.getTop(), for2.getRight(), for2.getBottom(), for1.getId());
        } else if (for1.getTop() == (for2.getBottom() + 1) && for1.getLeft() == for2.getLeft() && for1.getRight() == for2.getRight()) {
            return new FundamentalOpenRectangle(for2.getLeft(), for2.getTop(), for1.getRight(), for1.getBottom(), for1.getId());
        } else return null;
    }

    public Set<FundamentalOpenRectangle> getFOR() {
        return FOR;
    }

    public boolean isSliced() {
        return sliced;
    }
}
