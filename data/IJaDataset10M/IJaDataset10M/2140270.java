package com.wxz.sanguo.game.war;

import com.wxz.sanguo.common.Point;

/***
 * ���ӣ�ս��ĵ�λ��
 * 
 * @author wxz
 * 
 */
public class Troop {

    private int id;

    private int generalId;

    /***
	 *TODO ���ӵ��ƶ�����μ��㣿
	 */
    private int currentMove;

    private int baseMove;

    private Point warPoint;

    public Troop(int id, int generalId, int x, int y) {
        this.id = id;
        this.generalId = generalId;
        this.warPoint = new Point(x, y);
        this.currentMove = 10;
        this.baseMove = 10;
    }

    public int countMove(Point marchPoint) {
        return 0;
    }

    public int getGeneralId() {
        return generalId;
    }

    public Point getWarPoint() {
        return warPoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(int currentMove) {
        this.currentMove = currentMove;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
    }

    public void setWarPoint(Point warPoint) {
        this.warPoint = warPoint;
    }

    public int getBaseMove() {
        return baseMove;
    }

    public void setBaseMove(int baseMove) {
        this.baseMove = baseMove;
    }
}
