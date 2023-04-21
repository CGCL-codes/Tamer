
package org.makagiga.plugins.mapviewer;

import static java.awt.event.KeyEvent.*;

import static org.makagiga.commons.UI._;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.pushingpixels.trident.ease.Sine;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

import org.makagiga.commons.MDisposable;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.UI;
import org.makagiga.commons.fx.MTimeline;
import org.makagiga.commons.swing.MLabel;

public final class View extends JMapViewer implements MDisposable {

	// public
	
	public static final int DEFAULT_ZOOM = 3;
	
	// private

	private double rotateRadians;
	private int rotate;
	private MTimeline<View> moveAnimation;
	private MTimeline<View> rotateAnimation;
	private MTimeline<View> zoomAnimation;

	// public

	public View(final File cacheDir) {
		setFocusable(true);
	
		try {//!!!dir
			setTileLoader(new OsmFileCacheTileLoader(this, cacheDir));
		}
		catch (IOException exception) {
			MLogger.exception(exception);
		}

// TODO: help
		addKeyListener(new KeyAdapter() {//!!!leak: key adapter, default map controller
			@Override
			public void keyPressed(final KeyEvent e) {
				int speedX;
				int speedY;
				if (e.isShiftDown()) {
					speedX = getWidth();
					speedY = getHeight();
				}
				else {
					speedX = Math.min(200, getWidth());
					speedY = Math.min(200, getHeight());
				}
				
				Point newCenter = null;
				if (moveAnimation != null)
					moveAnimation.end(); // set "center" value

				switch (e.getKeyCode()) {
					case VK_UP:
						newCenter = new Point(center.x, center.y - speedY);
						break;
					case VK_DOWN:
						newCenter = new Point(center.x, center.y + speedY);
						break;
					case VK_LEFT:
						newCenter = new Point(center.x - speedX, center.y);
						break;
					case VK_RIGHT:
						newCenter = new Point(center.x + speedX, center.y);
						break;
				}
				
				if (newCenter != null) {
					moveAnimation = new MTimeline<>(View.this, 200);
					moveAnimation.setEase(new Sine());
					moveAnimation.addPropertyToInterpolate("center", getCenter(), newCenter);
					moveAnimation.play();
				}
			}
		} );
	}
	
	public Point getCenter() {
		return new Point(center);
	}
	
	public void setCenter(final Point value) {
		center.setLocation(value);
		repaint();
	}

	public int getRotate() { return rotate; }

	public void setRotate(int rotate) {
		if (rotate != this.rotate) {
			this.rotate = rotate;
			rotateRadians = Math.toRadians(rotate);
			repaint();
		}
	}

	public TileSource getTileSource() {
		return tileController.getTileSource();
	}

// FIXME: method name typo?
	@Override
	public void setZoomContolsVisible(final boolean visible) {
		super.setZoomContolsVisible(visible);
// TODO: adjust position, show
		zoomInButton.setVisible(false);
		zoomOutButton.setVisible(false);
	}
	
	@Override
	public void setTileSource(final TileSource tileSource) {
		super.setTileSource(tileSource);
		initializeZoomSliderLabels();
	}
	
	// MDisposable
	
	@Override
	public void dispose() {
		if (tileController != null) {
			tileController.setTileCache(null);
			tileController.setTileLoader(null);
			tileController.setTileSource(null);
		
			tileController.cancelOutstandingJobs();
			tileController = null;
		}
		
		if (mapMarkerList != null)
			mapMarkerList.clear();
		if (mapRectangleList != null)
			mapRectangleList.clear();
		
		zoomSlider = null;
		zoomInButton = null;
		zoomOutButton = null;
	}
	
	// protected
	
	@Override
	protected void initializeZoomSlider() {
		super.initializeZoomSlider();
		zoomInButton.setVisible(false);
		zoomOutButton.setVisible(false);
		initializeZoomSliderLabels();
		
		// fix hardcoded component size
		Dimension size = new Dimension(zoomSlider.getPreferredSize().width, zoomSlider.getHeight());
		zoomSlider.setSize(size);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics.create();
		if (rotate != 0) {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.transform(AffineTransform.getRotateInstance(rotateRadians, getWidth() / 2, getHeight() / 2));
		}
		super.paintComponent(g);
		g.dispose();
	}

	@Override
	protected void zoomChanged(final int oldZoom) {
		super.zoomChanged(oldZoom);
		zoomSlider.setToolTipText(_("Zoom (level {0})", zoom));
		zoomInButton.setToolTipText(_("Zoom In (level {0})", (zoom + 1)));
		zoomOutButton.setToolTipText(_("Zoom Out (level {0})", (zoom - 1)));
	}

	// private

	private void initializeZoomSliderLabels() {
		Hashtable<Integer, MLabel> labelTable = new Hashtable<>();
		MLabel plus = new MLabel("+");
		MLabel minus = new MLabel("-");
		UI.setStyle("font-size: larger; font-weight: bold", plus, minus);
		labelTable.put(zoomSlider.getMinimum(), minus);
		labelTable.put(zoomSlider.getMaximum(), plus);
		zoomSlider.setLabelTable(labelTable);
		zoomSlider.setPaintLabels(true);
	}
	
	// package

	void animatedRotate(final int relValue) {
		if (rotateAnimation != null)
			rotateAnimation.end();
		rotateAnimation = new MTimeline<>(this, 200);
		rotateAnimation.setEase(new Sine());
		rotateAnimation.addPropertyToInterpolate("rotate", getRotate(), getRotate() + relValue);
		rotateAnimation.play();
	}

// TODO: zoom animation
	void animatedZoom(final int absValue) {
		if (zoomAnimation != null)
			zoomAnimation.end();
/*		if (animation) {
			zoomAnimation = new MTimeline<>(this, 500);
			zoomAnimation.setEase(new Sine());
			zoomAnimation.addPropertyToInterpolate("zoom", getZoom(), absValue);
			zoomAnimation.play();
		}
		else {
*/
			setZoom(absValue);
//		}
	}

}
