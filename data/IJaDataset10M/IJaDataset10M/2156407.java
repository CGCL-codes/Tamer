// Copyright 2007 Konrad Twardowski
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

package org.makagiga.chart;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import org.makagiga.commons.TK;
import org.makagiga.commons.TriBoolean;
import org.makagiga.commons.annotation.InvokedFromConstructor;
import org.makagiga.commons.swing.MComponent;
import org.makagiga.commons.swing.event.MMouseAdapter;

/**
 * @since 2.0
 */
public class ChartView<M extends ChartModel> extends MComponent {

	// private
	
	private ChartPainter<M> painter;
	
	// public

	/**
	 * Constructs a new chart component with sample "RGB" model.
	 */
	@SuppressWarnings("unchecked")
	public ChartView() {
		this((M)new ChartModel.Sample());
	}

	/**
	 * Constructs a new chart component.
	 *
	 * @param model The model
	 *
	 * @throws NullPointerException If @p model is @c null
	 */
	public ChartView(final M model) {
		this(new ChartPainter<>(model));
	}

	/**
	 * Constructs a new chart component.
	 *
	 * @param painter The painter
	 *
	 * @throws NullPointerException If @p painter is @c null
	 */
	public ChartView(final ChartPainter<M> painter) {
		setOpaque(true);
		setPainter(painter);

		StaticHandler handler = new StaticHandler();
		addMouseListener(handler);
		addMouseMotionListener(handler);
	}
	
	public void addActionListener(final ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}
	
	public ActionListener[] getActionListeners() {
		return listenerList.getListeners(ActionListener.class);
	}

	public void removeActionListener(final ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	public ChartModel.Item getActiveItem() {
		return painter.activeItem;
	}
	
	public ChartPainter<M> getPainter() { return painter; }
	
	@InvokedFromConstructor
	public void setPainter(final ChartPainter<M> value) {
		TriBoolean oldInteractive = TriBoolean.UNDEFINED;
		if (painter != null) {
			oldInteractive = TriBoolean.of(painter.interactive);
		}

		painter = TK.checkNull(value, "value");
		
		if (oldInteractive.isDefined())
			painter.interactive = oldInteractive.isTrue();
		
		repaint();
	}
	
	public boolean isInteractive() {
		return painter.interactive;
	}
	
	public void setInteractive(final boolean value) {
		painter.interactive = value;
	}
	
	// protected
	
	protected void fireActionPerformed() {
		TK.fireActionPerformed(this, getActionListeners());
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (painter != null)
			painter.paint(this, (Graphics2D)g);
	}

	// private classes

	private static final class StaticHandler extends MMouseAdapter {

		// public

		@Override
		public void mouseClicked(final MouseEvent e) {
			ChartView<?> chartView = (ChartView<?>)e.getSource();
			if (chartView.painter.interactive && (chartView.painter.activeItem != null))
				chartView.fireActionPerformed();
		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			ChartView<?> chartView = (ChartView<?>)e.getSource();
			if (chartView.painter.interactive) {
				chartView.repaint();
				int type =
					(!chartView.painter.interactive || (chartView.painter.activeItem == null))
					? Cursor.DEFAULT_CURSOR
					: Cursor.HAND_CURSOR;
				chartView.setCursor(type);
			}
		}

	}

}
