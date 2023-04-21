package Interface.JGraph_Algebra;

import java.util.Vector;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import javax.swing.ImageIcon;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import Mapeamento.Atributos.*;
import Mapeamento.Tabelas.*;

/**
 *
 * @author ra044637
 */
public class Selecao extends DefaultGraphCell {

    Consulta consulta;

    Consulta c_pai;

    /** Creates a new instance of Selecao */
    public Selecao(Consulta consulta, double x, double y, double w, double h, String condicoes) {
        super(condicoes);
        ImageIcon icon;
        icon = new ImageIcon(getClass().getClassLoader().getResource("Interface/Icones/selecao.png"));
        GraphConstants.setIcon(this.getAttributes(), icon);
        GraphConstants.setBounds(this.getAttributes(), new Rectangle2D.Double(x, y, w, h));
        this.addPort();
        this.consulta = consulta;
    }

    public Consulta getConsulta() {
        return this.consulta;
    }

    public void setConsultaP(Consulta consulta) {
        this.c_pai = consulta;
    }

    public Consulta getConsultaP() {
        return this.c_pai;
    }
}
