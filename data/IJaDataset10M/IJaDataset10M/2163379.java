package com.lemu.leco.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import ren.gui.components.DragSourceJPanel;
import ren.gui.seqEdit.NoteToGraphicsConverter;
import ren.gui.seqEdit.ParamNTGC;
import com.lemu.gui.TraseMorphPanelSmall;
import com.lemu.music.PartMorph;
import com.lemu.music.morph.rt.MorpherRT;
import com.lemu.music.morph.transearch.TraseMorph;

public class EditMorphPartBlock extends DragSourceJPanel {

    public PartMorph pm;

    public NoteToGraphicsConverter note2graph;

    public EditMorphPartBlock() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.setPreferredSize(new Dimension(EditPanel.partMorphWid, EditPanel.partMorphHei));
    }

    public void construct(PartMorph pm, ParamNTGC note2graph) {
        this.removeAll();
        this.pm = pm;
        this.note2graph = note2graph;
        MorpherRT morpher = (MorpherRT) pm.getMorphStrucList().getSelectedItem();
        if (morpher instanceof TraseMorph) {
            TraseMorphPanelSmall tmp = new TraseMorphPanelSmall();
            tmp.construct((TraseMorph) morpher, note2graph);
            this.add(tmp);
        }
    }
}
