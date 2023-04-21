package net.sourceforge.f2ibuilder.application.view.image.draw.metric;

import java.awt.Dimension;
import java.awt.Point;
import net.sourceforge.f2ibuilder.application.model.FontText;
import net.sourceforge.f2ibuilder.components.type.Counter;

/**
 * Algoritmo para Aplica��o de M�tricas a Fonte.
 * 
 * Design Pattern: GoF - Strategy 
 * 
 * @author David Ferreira - davidferreira.fz@gmail.com
 */
public abstract class MetricStrategy {

    public abstract Dimension adjust(Dimension ajuste, Dimension posicao, FontText fontText);

    public abstract Point position(Counter count, Dimension posicao, Dimension ajuste, FontText fontText);
}
