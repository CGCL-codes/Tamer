// Copyright 2012 Konrad Twardowski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.makagiga.garland;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;

import org.makagiga.commons.TK;
import org.makagiga.commons.UI;
import org.makagiga.commons.swing.event.MMouseAdapter;

public class GarlandDivider {

	// private
	
	private final Handler handler = new Handler();
	private int location = -1;
	private final WeakReference<GarlandBox> garlandBoxRef;
	
	// public
	
	public GarlandDivider(final GarlandBox box) {
		garlandBoxRef = new WeakReference<>(box);
		box.addMouseListener(handler);
		box.addMouseMotionListener(handler);
	}

	public int getLocation() { return location; }
	
	public void setLocation(final int value) {
		if (value != location) {
			System.err.println("SET LOCATION:"+value);
			location = value;
			GarlandBox box = TK.get(garlandBoxRef);
			if (box != null) {
				if (box.getPreferredWidth() != location) {
					box.setPreferredWidth(location);
					GarlandPanel gp = UI.getAncestorOfClass(GarlandPanel.class, box);
					if (gp != null) {
						gp.revalidate();//!!!
						gp.getHeader().repaint();
					}
				}
			}
		}
	}
	
	public void paint(final Graphics2D g) {
		GarlandBox box = TK.get(garlandBoxRef);
		
		if (box == null)
			return;
	
		Rectangle area = getArea();
		Point mp = box.getMousePosition();
		if ((mp != null) && area.contains(mp)) {
			g.setColor(box.getStyle().getBaseColor());
			g.fill(area);
		}
	}
	
	// private
	
	private Rectangle getArea() {
		GarlandBox box = TK.get(garlandBoxRef);
		
		if (box == null)
			return new Rectangle();
		
		int bw = box.getWidth();
		int bh = box.getHeight();
		int dw = 5;
		
		return new Rectangle(bw - dw, 0, dw, bh);
	}
	
	// private classes
	
	private static final class Handler extends MMouseAdapter {
	
		// private
		
		private boolean inArea;
		private int startX = -1;
	
		// public

		@Override
		public void mouseDragged(final MouseEvent e) {
			if (startX == -1)
				return;
		
			int x = e.getX();
			GarlandDivider d = getDivider(e);
			int l = d.getLocation();
			if (l == -1)
				d.setLocation(x);
			else
				d.setLocation(Math.max(100, l + (x - startX)));
			startX = x;
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			if (inArea) {
				inArea = false;
				GarlandBox box =(GarlandBox)e.getSource();
				box.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				box.repaint(getDivider(e).getArea());
			}
		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			Rectangle area = getDivider(e).getArea();
			boolean newInArea = area.contains(e.getPoint());
			if (newInArea != inArea) {
				inArea = newInArea;
				GarlandBox box =(GarlandBox)e.getSource();
				box.setCursor(Cursor.getPredefinedCursor(inArea ? Cursor.E_RESIZE_CURSOR : Cursor.DEFAULT_CURSOR));
				box.repaint(area);
			}
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			Rectangle area = getDivider(e).getArea();
			startX = area.contains(e.getPoint()) ? e.getX() : -1;
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			startX = -1;
		}

		// private
		
		private GarlandDivider getDivider(final MouseEvent e) {
			GarlandBox box = (GarlandBox)e.getSource();
			
			return box.getDivider();
		}

	}

}
