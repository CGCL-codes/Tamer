package com.prolix.editor.interaction.files.elements.structs.test.questions;

import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LD_Property;
import com.prolix.editor.interaction.files.elements.BlockElement;
import com.prolix.editor.interaction.files.elements.basicStruct.BlockHTMLStructDiv;
import com.prolix.editor.interaction.files.elements.basicStruct.BlockHTMLStructOL;
import com.prolix.editor.interaction.files.elements.basicStruct.BlockHTMLStructP;
import com.prolix.editor.interaction.files.elements.basicStruct.BlockStructText;
import com.prolix.editor.interaction.files.elements.property.BlockSetProperty;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class BlockTestDefineMCQuestion implements BlockElement {

    private LD_Property question;

    private List possibleAnswers;

    private LD_Property solution;

    private BlockHTMLStructDiv main;

    public BlockTestDefineMCQuestion(LD_Property question, List possibleAnswers, LD_Property solution) {
        this.possibleAnswers = possibleAnswers;
        this.question = question;
        this.solution = solution;
        inizialise();
    }

    private void inizialise() {
        main = new BlockHTMLStructDiv();
        main.setClass("testQuestion");
        main.add(new BlockHTMLStructP(new BlockStructText("Question:")));
        inizialisePropertyQuestionAnswers();
    }

    private void inizialisePropertyQuestionAnswers() {
        main.add(new BlockHTMLStructP(new BlockSetProperty(question)));
        Iterator it = possibleAnswers.iterator();
        if (!it.hasNext()) return;
        BlockHTMLStructOL list = new BlockHTMLStructOL();
        main.add(list);
        while (it.hasNext()) list.add(new BlockSetProperty((LD_Property) it.next()));
        main.add(new BlockHTMLStructP(new BlockStructText("Correct Answer:")));
        main.add(new BlockHTMLStructP(new BlockSetProperty(solution)));
    }

    public void build(Element root) {
        main.build(root);
    }
}
