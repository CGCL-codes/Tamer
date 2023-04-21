package org.thechiselgroup.choosel.protovis.client;

import java.util.Comparator;
import org.thechiselgroup.choosel.protovis.client.MiserablesData.NovelCharacter;
import org.thechiselgroup.choosel.protovis.client.MiserablesData.NovelCharacterNodeAdapter;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsFunction;
import com.google.gwt.user.client.ui.Widget;

/**
 * Protovis/GWT implementation of <a
 * href="http://vis.stanford.edu/protovis/ex/matrix.html">Protovis matrix
 * diagram example</a>.
 * 
 * @author Lars Grammel
 */
public class MatrixDiagramExample extends ProtovisWidget implements ProtovisExample {

    @Override
    public Widget asWidget() {
        return this;
    }

    private void createVisualization(NovelCharacter[] nodes, Link[] links) {
        final PVOrdinalScale color = PV.Colors.category19();
        PVPanel vis = getPVPanel().width(693).height(693).top(90).left(90);
        PVMatrixLayout layout = vis.add(PV.Layout.Matrix()).nodes(new NovelCharacterNodeAdapter(), nodes).links(links).sort(new Comparator<PVNode>() {

            public int compare(PVNode a, PVNode b) {
                NovelCharacter ac = a.object();
                NovelCharacter bc = b.object();
                return bc.getGroup() - ac.getGroup();
            }
        });
        layout.link().add(PV.Bar).fillStyle(new JsFunction<PVColor>() {

            public PVColor f(JsArgs args) {
                PVLink l = args.getObject();
                if (l.linkValue() != 0) {
                    int targetGroup = l.targetNode().<NovelCharacter>object().getGroup();
                    int sourceGroup = l.sourceNode().<NovelCharacter>object().getGroup();
                    return (targetGroup == sourceGroup) ? color.fcolor(sourceGroup) : PV.color("#555");
                }
                return PV.color("#eee");
            }
        }).antialias(false).lineWidth(1);
        layout.label().add(PV.Label).textStyle(new JsFunction<PVColor>() {

            public PVColor f(JsArgs args) {
                PVNode node = args.getObject();
                return color.fcolor(node.<NovelCharacter>object().getGroup());
            }
        });
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getProtovisExampleURL() {
        return "http://vis.stanford.edu/protovis/ex/matrix.html";
    }

    public String getSourceCodeFile() {
        return "MatrixDiagramExample.java";
    }

    protected void onAttach() {
        super.onAttach();
        initPVPanel();
        createVisualization(MiserablesData.CHARACTERS, MiserablesData.LINKS);
        getPVPanel().render();
    }

    public String toString() {
        return "Matrix Diagram";
    }
}
