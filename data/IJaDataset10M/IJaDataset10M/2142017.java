package com.sd_editions.collatex.Web;

import java.io.Serializable;
import java.util.List;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import com.google.common.collect.Lists;
import com.sd_editions.collatex.Block.BlockStructure;
import com.sd_editions.collatex.Block.Util;
import com.sd_editions.collatex.Collate.LCS;
import com.sd_editions.collatex.Collate.Table;
import com.sd_editions.collatex.Collate.Tuple;
import com.sd_editions.collatex.Collate.TupleToTable;
import com.sd_editions.collatex.Collate.WordAlignmentVisitor;

@SuppressWarnings("serial")
public class Homepage extends WebPage {

    public Homepage() {
        ModelForView model = new ModelForView("the drought of march hath perced to the root and is this the right", new String[] { "the first march of drought pierced to the root and this is the ", "the first march of drought hath perced to the root" });
        add(new Label("base", new PropertyModel(model, "base")));
        add(new Label("witness1", new PropertyModel(model, "witness1")));
        add(new Label("witness2", new PropertyModel(model, "witness2")));
        Label label = new Label("alignment", new PropertyModel(model, "html"));
        label.setEscapeModelStrings(false);
        Label label2 = new Label("alignment2", new PropertyModel(model, "html2"));
        label2.setEscapeModelStrings(false);
        add(label);
        add(label2);
        add(new AlignmentForm("alignmentform", model));
    }

    @SuppressWarnings("serial")
    class ModelForView implements Serializable {

        private String base;

        private final String[] witnesses;

        private String html;

        private String html2;

        public ModelForView(String newBase, String[] newWitnesses) {
            this.base = newBase;
            this.witnesses = newWitnesses;
            fillAlignmentTable_LCS();
            fillAlignmentTable();
        }

        void fillAlignmentTable_LCS() {
            BlockStructure baseStructure = Util.string2BlockStructure(base.toLowerCase());
            List<BlockStructure> witnessList = Lists.newArrayList();
            List<Tuple[]> resultList = Lists.newArrayList();
            int i = 0;
            for (String witness : witnesses) {
                BlockStructure witnessStructure = Util.string2BlockStructure(witness.toLowerCase());
                witnessList.add(witnessStructure);
                LCS tupSeq = new LCS(baseStructure, witnessList, i);
                resultList.add(tupSeq.getLCS());
                i++;
            }
            Tuple[][] results = resultList.toArray(new Tuple[][] {});
            Table alignment = new TupleToTable(baseStructure, witnessList, results).getTable();
            this.html = alignment.toHTML();
        }

        void fillAlignmentTable() {
            BlockStructure baseStructure = Util.string2BlockStructure(base);
            List<BlockStructure> witnessList = Lists.newArrayList();
            List<Tuple[]> resultList = Lists.newArrayList();
            for (String witness : witnesses) {
                BlockStructure witnessStructure = Util.string2BlockStructure(witness);
                WordAlignmentVisitor visitor = new WordAlignmentVisitor(witnessStructure);
                baseStructure.accept(visitor);
                witnessList.add(witnessStructure);
                resultList.add(visitor.getResult());
            }
            Tuple[][] results = resultList.toArray(new Tuple[][] {});
            Table alignment = new TupleToTable(baseStructure, witnessList, results).getTable();
            this.html2 = alignment.toHTML();
        }

        public void setBase(String newBase) {
            this.base = newBase;
        }

        public String getBase() {
            return base;
        }

        public void setWitness1(String witness) {
            this.witnesses[0] = witness;
        }

        public String getWitness1() {
            return witnesses[0];
        }

        public void setWitness2(String witness) {
            this.witnesses[1] = witness;
        }

        public String getWitness2() {
            return witnesses[1];
        }

        public void setHtml(String newHtml) {
            this.html = newHtml;
        }

        public String getHtml() {
            return html;
        }

        public String getHtml2() {
            return html2;
        }
    }

    @SuppressWarnings("serial")
    class AlignmentForm extends Form {

        private final ModelForView modelForView;

        public AlignmentForm(String id, ModelForView myModelForView) {
            super(id);
            this.modelForView = myModelForView;
            add(new TextField("base", new PropertyModel(modelForView, "base")));
            add(new TextField("witness1", new PropertyModel(modelForView, "witness1")));
            add(new TextField("witness2", new PropertyModel(modelForView, "witness2")));
        }

        @Override
        protected void onSubmit() {
            modelForView.fillAlignmentTable();
            modelForView.fillAlignmentTable_LCS();
        }
    }
}
