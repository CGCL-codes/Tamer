package phaenuhr.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Arc2D;

public class LIKI_Pie extends PhenoPie {
	// private double[] mittelwertAbweichungen;

	protected double[] eintrittBluehBeginn;

	public LIKI_Pie(double durchmesser, double x, double y,
			double[] eintrittsTage, double[] eintrittBluehBeginn,
			Filling[] farben, Label[] txtInnerLabel, boolean innerLabelling,
			Label[] txtOuterLabel) {// , double[] mittelwertAbweichungen) {
		super(durchmesser, x, y, eintrittsTage, farben, txtInnerLabel,
				innerLabelling, txtOuterLabel);
		// this.mittelwertAbweichungen = mittelwertAbweichungen;
		this.eintrittBluehBeginn = eintrittBluehBeginn;
	}

	@Override
	protected void zeichneAussenBeschriftung(Graphics2D g) {

		Arc2D.Double pie = (Arc2D.Double) pies.get(0);
		double start = pie.getAngleStart();
		double extent = pie.getAngleExtent();
		try {
			// Vegetationsperiode
			double einfügeWinkel = 270D;
			Arc2D.Double k = new Arc2D.Double(x - distanceOuterLabelFromBorder
					/ 2, (y - distanceOuterLabelFromBorder / 2), durchmesser
					+ distanceOuterLabelFromBorder, durchmesser
					+ distanceOuterLabelFromBorder, einfügeWinkel, 0.0,
					Arc2D.PIE);
			int ausrichtung = Label.SUEDEN;
			getTxtOuterLabel()[0].setEinfuegeDaten(k.getStartPoint(),
					ausrichtung);
			getTxtOuterLabel()[0].zeichne(g);

			// VegetationsRuhe
			pie = (Arc2D.Double) pies.get(1);
			start = pie.getAngleStart();
			extent = pie.getAngleExtent();
			k.setAngleStart(start + (extent / 2));
			k.setAngleExtent(0.0);
			einfügeWinkel = k.getAngleStart();
			ausrichtung = Label.berechneAusrichtung(einfügeWinkel);
			getTxtOuterLabel()[1].setEinfuegeDaten(k.getStartPoint(),
					ausrichtung);
			getTxtOuterLabel()[1].zeichne(g);

			// Blühbeginn Salweide am Start der Veg.-Periode
			pie = (Arc2D.Double) pies.get(0);
			k.setAngleStart(pie.getAngleStart());

			ausrichtung = Label.berechneAusrichtung(k.getAngleStart());
			switch (ausrichtung) {
			case (Label.NORD_OST):
			case (Label.SUED_OST):
				ausrichtung = Label.OSTEN;
				break;
			case (Label.NORD_WEST):
			case (Label.SUED_WEST):
				ausrichtung = Label.WESTEN;
				break;
			}

			getTxtOuterLabel()[2].setEinfuegeDaten(k.getStartPoint(), ausrichtung);
			getTxtOuterLabel()[2].zeichne(g);

			// Blattverfärbung Stieleiche am Start der Veg.-Ruhe
			pie = (Arc2D.Double) pies.get(1);
			k.setAngleStart(pie.getAngleStart());

			ausrichtung = Label.berechneAusrichtung(k.getAngleStart());
			switch (ausrichtung) {
			case (Label.NORD_OST):
			case (Label.SUED_OST):
				ausrichtung = Label.OSTEN;
				break;
			case (Label.NORD_WEST):
			case (Label.SUED_WEST):
				ausrichtung = Label.WESTEN;
				break;
			}

			getTxtOuterLabel()[3].setEinfuegeDaten(k.getStartPoint(), ausrichtung);
			getTxtOuterLabel()[3].zeichne(g);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void zeichneLinienBeschriftungBluehBeginn(Graphics2D g2) {
		Arc2D.Double k = null;

		Label[] bluehbeginnLabels = null;
		try {
			bluehbeginnLabels = new Label[2];
			TextLine lineHasel = new TextLine("1", ((TextLine)getTxtOuterLabel()[4].getText().get(0)).getFont());
			bluehbeginnLabels[0] = new Label(new TextLine[]{lineHasel});
			TextLine lineApfel = new TextLine("2", ((TextLine)getTxtOuterLabel()[4].getText().get(0)).getFont());
			bluehbeginnLabels[1] = new Label(new TextLine[]{lineApfel});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < eintrittBluehBeginn.length; i++) {
			double start = berechneStartWinkel(eintrittBluehBeginn[i]);
			double end = tageNachGradZahl(1);

			k = new Arc2D.Double(x - distanceOuterLabelFromBorder / 2, y
					- distanceOuterLabelFromBorder / 2, durchmesser
					+ distanceOuterLabelFromBorder, durchmesser
					+ distanceOuterLabelFromBorder, start, end, Arc2D.PIE);

			if (getTxtOuterLabel() != null) {
				int ausrichtung = Label.berechneAusrichtung(k.getAngleStart());
				switch (ausrichtung) {
				case (Label.NORD_OST):
				case (Label.SUED_OST):
					ausrichtung = Label.OSTEN;
					break;
				case (Label.NORD_WEST):
				case (Label.SUED_WEST):
					ausrichtung = Label.WESTEN;
					break;
				}

				bluehbeginnLabels[i].setEinfuegeDaten(k.getStartPoint(),
						ausrichtung);
				bluehbeginnLabels[i].zeichne(g2);
			}

		}
	}

	protected void zeichneLinienBluehBeginn(Graphics2D g2) {
		Arc2D.Double k = null;

		for (int i = 0; i < eintrittBluehBeginn.length; i++) {

			double start = berechneStartWinkel(eintrittBluehBeginn[i] - 1D);
			double end = tageNachGradZahl(0.0);

			k = new Arc2D.Double(x, y, durchmesser, durchmesser, start, end,
					Arc2D.PIE);


			float[] dashPattern = { 5, 3, 5, 3 };

			Stroke backup = g2.getStroke();

			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10, dashPattern, 0));
			g2.setColor(Color.black);
			g2.draw(k);

			g2.setStroke(backup);
		}
	}

	public void zeichnePieRaender(Graphics2D g) {
		// Schwarze Ränder um die normal gefüllten Arcs zeichnen
		for (int i = 0; i < pies.size(); i++) {
			Arc2D.Double k = (Arc2D.Double) pies.get(i);
			//g.setColor(farben[i]);
			g.setColor(Color.black);
			g.draw(k);
		}
	}

	@Override
	public void drawPie(Graphics2D g) {

		zeichneKuchenStuecke(g);
		zeichneLinienBluehBeginn(g);
		// INNERE BESCHRIFTUNG
		if (innerLabelling) {
			zeichneInnereBeschriftung(g);
		}
		// *********************************************************************
		// AUSSENBESCHRIFTUNG

		if (txtOuterLabel != null) {
			zeichneAussenBeschriftung(g);
			zeichneLinienBeschriftungBluehBeginn(g);
		}

	}

	public double[] getEintrittBluehBeginn() {
		return eintrittBluehBeginn;
	}

	public void setEintrittBluehBeginn(double[] eintrittBluehBeginn) {
		this.eintrittBluehBeginn = eintrittBluehBeginn;
	}

}
