package com.moesol.gwt.maps.client;

public class DragTracker {

    private ViewCoords m_origPort = new ViewCoords();

    private ViewCoords m_deltaPort = new ViewCoords();

    private WorldCoords m_origWorld = new WorldCoords();

    private boolean m_sameAsLast = false;

    public DragTracker(int x, int y, WorldCoords center) {
        m_origPort = new ViewCoords(x, y);
        m_origWorld = center;
    }

    public void set(int x, int y, ViewCoords center) {
        m_origPort = new ViewCoords(x, y);
        m_origWorld = new WorldCoords(center.getX(), center.getY());
    }

    public WorldCoords update(int x, int y) {
        int newDeltaPortX = x - m_origPort.getX();
        int newDeltaPortY = m_origPort.getY() - y;
        computeSameAsLast(newDeltaPortX, newDeltaPortY);
        m_deltaPort = new ViewCoords(newDeltaPortX, newDeltaPortY);
        return WorldCoords.builder().setX(m_origWorld.getX() - m_deltaPort.getX()).setY(m_origWorld.getY() - m_deltaPort.getY()).build();
    }

    public ViewCoords updateView(int x, int y) {
        int newDeltaPortX = x - m_origPort.getX();
        int newDeltaPortY = m_origPort.getY() - y;
        computeSameAsLast(newDeltaPortX, newDeltaPortY);
        m_deltaPort = new ViewCoords(newDeltaPortX, newDeltaPortY);
        return ViewCoords.builder().setX(m_origWorld.getX() - m_deltaPort.getX()).setY(m_origWorld.getY() - m_deltaPort.getY()).build();
    }

    private void computeSameAsLast(int newDeltaPortX, int newDeltaPortY) {
        if (newDeltaPortX != m_deltaPort.getX()) {
            m_sameAsLast = false;
            return;
        }
        if (newDeltaPortY != m_deltaPort.getY()) {
            m_sameAsLast = false;
            return;
        }
        m_sameAsLast = true;
    }

    public boolean isSameAsLast() {
        return m_sameAsLast;
    }
}
