package org.cleartk.syntax.dependency.malt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.cleartk.syntax.dependency.type.DependencyNode;
import org.cleartk.syntax.dependency.type.TopDependencyNode;
import org.cleartk.test.CleartkTestBase;
import org.cleartk.token.type.Sentence;
import org.cleartk.token.type.Token;
import org.junit.Test;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.testing.factory.TokenBuilder;
import org.uimafit.util.JCasUtil;

/**
 * <br>
 * Copyright (c) 2010, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public class MaltParserTest extends CleartkTestBase {

    @Override
    public String[] getTypeSystemDescriptorNames() {
        return new String[] { "org.cleartk.token.TypeSystem", "org.cleartk.syntax.dependency.TypeSystem" };
    }

    @Test
    public void test() throws UIMAException {
        this.assumeBigMemoryTestsEnabled();
        this.logger.info(BIG_MEMORY_TEST_MESSAGE);
        TokenBuilder<Token, Sentence> tokenBuilder = new TokenBuilder<Token, Sentence>(Token.class, Sentence.class, "pos", "stem");
        tokenBuilder.buildTokens(this.jCas, "The dog chased the fox down the road.", "The dog chased the fox down the road .", "DT NN VBD DT NN IN DT NN .");
        AnalysisEngineDescription desc = MaltParser.getDescription();
        AnalysisEngine engine = AnalysisEngineFactory.createPrimitive(desc);
        engine.process(this.jCas);
        engine.collectionProcessComplete();
        Collection<DependencyNode> nodes;
        nodes = JCasUtil.select(jCas, DependencyNode.class);
        String[] texts = "The dog chased the fox down the road .".split(" ");
        assertEquals(texts.length, nodes.size());
        Iterator<DependencyNode> nodesIter = nodes.iterator();
        for (int i = 0; i < texts.length; ++i) {
            assertEquals(texts[i], nodesIter.next().getCoveredText());
        }
        nodesIter = nodes.iterator();
        DependencyNode the1 = nodesIter.next();
        DependencyNode dog = nodesIter.next();
        DependencyNode chased = nodesIter.next();
        DependencyNode the2 = nodesIter.next();
        DependencyNode fox = nodesIter.next();
        DependencyNode down = nodesIter.next();
        DependencyNode the3 = nodesIter.next();
        DependencyNode road = nodesIter.next();
        DependencyNode period = nodesIter.next();
        assertEquals(1, the1.getHeadRelations().size());
        assertEquals(dog, the1.getHeadRelations(0).getHead());
        assertEquals("det", the1.getHeadRelations(0).getRelation());
        assertEquals(1, dog.getHeadRelations().size());
        assertEquals(chased, dog.getHeadRelations(0).getHead());
        assertEquals("nsubj", dog.getHeadRelations(0).getRelation());
        assertEquals(0, chased.getHeadRelations().size());
        assertTrue(chased instanceof TopDependencyNode);
        assertEquals(1, the2.getHeadRelations().size());
        assertEquals(fox, the2.getHeadRelations(0).getHead());
        assertEquals("det", the2.getHeadRelations(0).getRelation());
        assertEquals(1, fox.getHeadRelations().size());
        assertEquals(chased, fox.getHeadRelations(0).getHead());
        assertEquals("dobj", fox.getHeadRelations(0).getRelation());
        assertEquals(1, down.getHeadRelations().size());
        assertEquals(chased, down.getHeadRelations(0).getHead());
        assertEquals("advmod", down.getHeadRelations(0).getRelation());
        assertEquals(1, the3.getHeadRelations().size());
        assertEquals(road, the3.getHeadRelations(0).getHead());
        assertEquals("det", the3.getHeadRelations(0).getRelation());
        assertEquals(1, road.getHeadRelations().size());
        assertEquals(down, road.getHeadRelations(0).getHead());
        assertEquals("pobj", road.getHeadRelations(0).getRelation());
        assertEquals(1, period.getHeadRelations().size());
        assertEquals(chased, period.getHeadRelations(0).getHead());
        assertEquals("punct", period.getHeadRelations(0).getRelation());
        List<DependencyNode> emptyList = Collections.emptyList();
        assertEquals(emptyList, getChildren(the1));
        assertEquals(Arrays.asList(the1), getChildren(dog));
        assertEquals(Arrays.asList(dog, fox, down, period), getChildren(chased));
        assertEquals(emptyList, getChildren(the2));
        assertEquals(Arrays.asList(the2), getChildren(fox));
        assertEquals(Arrays.asList(road), getChildren(down));
        assertEquals(emptyList, getChildren(the3));
        assertEquals(Arrays.asList(the3), getChildren(road));
        assertEquals(emptyList, getChildren(period));
    }

    private static List<DependencyNode> getChildren(DependencyNode node) {
        List<DependencyNode> children = new ArrayList<DependencyNode>();
        for (int i = 0; i < node.getChildRelations().size(); ++i) {
            children.add(node.getChildRelations(i).getChild());
        }
        return children;
    }
}
