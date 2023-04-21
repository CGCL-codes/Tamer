package com.cell.math;

/**
 * 向量，在2D坐标系内，拥有方向和长度的单位
 * @author WAZA
 *
 */
public interface Vector {

    double getVectorX();

    double getVectorY();

    void setVectorX(double x);

    void setVectorY(double y);

    void addVectorX(double dx);

    void addVectorY(double dy);
}
