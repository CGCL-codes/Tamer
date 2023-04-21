package urban.services;

import com.google.inject.Singleton;
import com.google.inject.Inject;
import org.eclipse.xtext.*;
import org.eclipse.xtext.service.GrammarProvider;
import org.eclipse.xtext.service.AbstractElementFinder.*;

@Singleton
public class UrbanGrammarAccess extends AbstractGrammarElementFinder {

    public class ModelElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Model");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Assignment cImportsAssignment_0 = (Assignment) cAlternatives.eContents().get(0);

        private final RuleCall cImportsImportsParserRuleCall_0_0 = (RuleCall) cImportsAssignment_0.eContents().get(0);

        private final Assignment cElementsAssignment_1 = (Assignment) cAlternatives.eContents().get(1);

        private final RuleCall cElementsLineParserRuleCall_1_0 = (RuleCall) cElementsAssignment_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Assignment getImportsAssignment_0() {
            return cImportsAssignment_0;
        }

        public RuleCall getImportsImportsParserRuleCall_0_0() {
            return cImportsImportsParserRuleCall_0_0;
        }

        public Assignment getElementsAssignment_1() {
            return cElementsAssignment_1;
        }

        public RuleCall getElementsLineParserRuleCall_1_0() {
            return cElementsLineParserRuleCall_1_0;
        }
    }

    public class LineElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Line");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Assignment cLineAssignment_0 = (Assignment) cAlternatives.eContents().get(0);

        private final RuleCall cLineCommentLineParserRuleCall_0_0 = (RuleCall) cLineAssignment_0.eContents().get(0);

        private final Group cGroup_1 = (Group) cAlternatives.eContents().get(1);

        private final Keyword cInitKeyword_1_0 = (Keyword) cGroup_1.eContents().get(0);

        private final Assignment cLineAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cLineInitialiseParserRuleCall_1_1_0 = (RuleCall) cLineAssignment_1_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_1_2 = (RuleCall) cGroup_1.eContents().get(2);

        private final Group cGroup_2 = (Group) cAlternatives.eContents().get(2);

        private final Keyword cModKeyword_2_0 = (Keyword) cGroup_2.eContents().get(0);

        private final Assignment cLineAssignment_2_1 = (Assignment) cGroup_2.eContents().get(1);

        private final RuleCall cLineModificationParserRuleCall_2_1_0 = (RuleCall) cLineAssignment_2_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_2_2 = (RuleCall) cGroup_2.eContents().get(2);

        private final Group cGroup_3 = (Group) cAlternatives.eContents().get(3);

        private final Keyword cObsKeyword_3_0 = (Keyword) cGroup_3.eContents().get(0);

        private final Assignment cLineAssignment_3_1 = (Assignment) cGroup_3.eContents().get(1);

        private final RuleCall cLineObserveParserRuleCall_3_1_0 = (RuleCall) cLineAssignment_3_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_3_2 = (RuleCall) cGroup_3.eContents().get(2);

        private final Group cGroup_4 = (Group) cAlternatives.eContents().get(4);

        private final Keyword cVarKeyword_4_0 = (Keyword) cGroup_4.eContents().get(0);

        private final Assignment cLineAssignment_4_1 = (Assignment) cGroup_4.eContents().get(1);

        private final RuleCall cLineObserveParserRuleCall_4_1_0 = (RuleCall) cLineAssignment_4_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_4_2 = (RuleCall) cGroup_4.eContents().get(2);

        private final Group cGroup_5 = (Group) cAlternatives.eContents().get(5);

        private final Keyword cCausalKeyword_5_0 = (Keyword) cGroup_5.eContents().get(0);

        private final Assignment cLineAssignment_5_1 = (Assignment) cGroup_5.eContents().get(1);

        private final RuleCall cLineStoryParserRuleCall_5_1_0 = (RuleCall) cLineAssignment_5_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_5_2 = (RuleCall) cGroup_5.eContents().get(2);

        private final Group cGroup_6 = (Group) cAlternatives.eContents().get(6);

        private final Assignment cLineAssignment_6_0 = (Assignment) cGroup_6.eContents().get(0);

        private final RuleCall cLineRuleParserRuleCall_6_0_0 = (RuleCall) cLineAssignment_6_0.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_6_1 = (RuleCall) cGroup_6.eContents().get(1);

        private final Group cGroup_7 = (Group) cAlternatives.eContents().get(7);

        private final Keyword cGenKeyword_7_0 = (Keyword) cGroup_7.eContents().get(0);

        private final Assignment cLineAssignment_7_1 = (Assignment) cGroup_7.eContents().get(1);

        private final RuleCall cLineGeneratorParserRuleCall_7_1_0 = (RuleCall) cLineAssignment_7_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_7_2 = (RuleCall) cGroup_7.eContents().get(2);

        private final Group cGroup_8 = (Group) cAlternatives.eContents().get(8);

        private final Keyword cShapeKeyword_8_0 = (Keyword) cGroup_8.eContents().get(0);

        private final Assignment cLineAssignment_8_1 = (Assignment) cGroup_8.eContents().get(1);

        private final RuleCall cLineShapeParserRuleCall_8_1_0 = (RuleCall) cLineAssignment_8_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_8_2 = (RuleCall) cGroup_8.eContents().get(2);

        private final Group cGroup_9 = (Group) cAlternatives.eContents().get(9);

        private final Keyword cLambdaKeyword_9_0 = (Keyword) cGroup_9.eContents().get(0);

        private final Assignment cLineAssignment_9_1 = (Assignment) cGroup_9.eContents().get(1);

        private final RuleCall cLineLambdaParserRuleCall_9_1_0 = (RuleCall) cLineAssignment_9_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_9_2 = (RuleCall) cGroup_9.eContents().get(2);

        private final RuleCall cEOLTerminalRuleCall_10 = (RuleCall) cAlternatives.eContents().get(10);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Assignment getLineAssignment_0() {
            return cLineAssignment_0;
        }

        public RuleCall getLineCommentLineParserRuleCall_0_0() {
            return cLineCommentLineParserRuleCall_0_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Keyword getInitKeyword_1_0() {
            return cInitKeyword_1_0;
        }

        public Assignment getLineAssignment_1_1() {
            return cLineAssignment_1_1;
        }

        public RuleCall getLineInitialiseParserRuleCall_1_1_0() {
            return cLineInitialiseParserRuleCall_1_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_1_2() {
            return cEOLTerminalRuleCall_1_2;
        }

        public Group getGroup_2() {
            return cGroup_2;
        }

        public Keyword getModKeyword_2_0() {
            return cModKeyword_2_0;
        }

        public Assignment getLineAssignment_2_1() {
            return cLineAssignment_2_1;
        }

        public RuleCall getLineModificationParserRuleCall_2_1_0() {
            return cLineModificationParserRuleCall_2_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_2_2() {
            return cEOLTerminalRuleCall_2_2;
        }

        public Group getGroup_3() {
            return cGroup_3;
        }

        public Keyword getObsKeyword_3_0() {
            return cObsKeyword_3_0;
        }

        public Assignment getLineAssignment_3_1() {
            return cLineAssignment_3_1;
        }

        public RuleCall getLineObserveParserRuleCall_3_1_0() {
            return cLineObserveParserRuleCall_3_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_3_2() {
            return cEOLTerminalRuleCall_3_2;
        }

        public Group getGroup_4() {
            return cGroup_4;
        }

        public Keyword getVarKeyword_4_0() {
            return cVarKeyword_4_0;
        }

        public Assignment getLineAssignment_4_1() {
            return cLineAssignment_4_1;
        }

        public RuleCall getLineObserveParserRuleCall_4_1_0() {
            return cLineObserveParserRuleCall_4_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_4_2() {
            return cEOLTerminalRuleCall_4_2;
        }

        public Group getGroup_5() {
            return cGroup_5;
        }

        public Keyword getCausalKeyword_5_0() {
            return cCausalKeyword_5_0;
        }

        public Assignment getLineAssignment_5_1() {
            return cLineAssignment_5_1;
        }

        public RuleCall getLineStoryParserRuleCall_5_1_0() {
            return cLineStoryParserRuleCall_5_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_5_2() {
            return cEOLTerminalRuleCall_5_2;
        }

        public Group getGroup_6() {
            return cGroup_6;
        }

        public Assignment getLineAssignment_6_0() {
            return cLineAssignment_6_0;
        }

        public RuleCall getLineRuleParserRuleCall_6_0_0() {
            return cLineRuleParserRuleCall_6_0_0;
        }

        public RuleCall getEOLTerminalRuleCall_6_1() {
            return cEOLTerminalRuleCall_6_1;
        }

        public Group getGroup_7() {
            return cGroup_7;
        }

        public Keyword getGenKeyword_7_0() {
            return cGenKeyword_7_0;
        }

        public Assignment getLineAssignment_7_1() {
            return cLineAssignment_7_1;
        }

        public RuleCall getLineGeneratorParserRuleCall_7_1_0() {
            return cLineGeneratorParserRuleCall_7_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_7_2() {
            return cEOLTerminalRuleCall_7_2;
        }

        public Group getGroup_8() {
            return cGroup_8;
        }

        public Keyword getShapeKeyword_8_0() {
            return cShapeKeyword_8_0;
        }

        public Assignment getLineAssignment_8_1() {
            return cLineAssignment_8_1;
        }

        public RuleCall getLineShapeParserRuleCall_8_1_0() {
            return cLineShapeParserRuleCall_8_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_8_2() {
            return cEOLTerminalRuleCall_8_2;
        }

        public Group getGroup_9() {
            return cGroup_9;
        }

        public Keyword getLambdaKeyword_9_0() {
            return cLambdaKeyword_9_0;
        }

        public Assignment getLineAssignment_9_1() {
            return cLineAssignment_9_1;
        }

        public RuleCall getLineLambdaParserRuleCall_9_1_0() {
            return cLineLambdaParserRuleCall_9_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_9_2() {
            return cEOLTerminalRuleCall_9_2;
        }

        public RuleCall getEOLTerminalRuleCall_10() {
            return cEOLTerminalRuleCall_10;
        }
    }

    public class ImportsElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Imports");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Keyword cImportKeyword_0 = (Keyword) cGroup.eContents().get(0);

        private final Assignment cImportURIAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cImportURISTRINGTerminalRuleCall_1_0 = (RuleCall) cImportURIAssignment_1.eContents().get(0);

        private final RuleCall cEOLTerminalRuleCall_2 = (RuleCall) cGroup.eContents().get(2);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Keyword getImportKeyword_0() {
            return cImportKeyword_0;
        }

        public Assignment getImportURIAssignment_1() {
            return cImportURIAssignment_1;
        }

        public RuleCall getImportURISTRINGTerminalRuleCall_1_0() {
            return cImportURISTRINGTerminalRuleCall_1_0;
        }

        public RuleCall getEOLTerminalRuleCall_2() {
            return cEOLTerminalRuleCall_2;
        }
    }

    public class LambdaElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Lambda");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cGeneratorAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final CrossReference cGeneratorGeneratorCrossReference_0_0 = (CrossReference) cGeneratorAssignment_0.eContents().get(0);

        private final RuleCall cGeneratorGeneratorSTRINGTerminalRuleCall_0_0_1 = (RuleCall) cGeneratorGeneratorCrossReference_0_0.eContents().get(1);

        private final Assignment cShapeAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final CrossReference cShapeShapeCrossReference_1_0 = (CrossReference) cShapeAssignment_1.eContents().get(0);

        private final RuleCall cShapeShapeSTRINGTerminalRuleCall_1_0_1 = (RuleCall) cShapeShapeCrossReference_1_0.eContents().get(1);

        private final Keyword cColonEqualsSignKeyword_2 = (Keyword) cGroup.eContents().get(2);

        private final Keyword cHyphenMinusKeyword_3 = (Keyword) cGroup.eContents().get(3);

        private final Assignment cValueAssignment_4 = (Assignment) cGroup.eContents().get(4);

        private final RuleCall cValueNumberParserRuleCall_4_0 = (RuleCall) cValueAssignment_4.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getGeneratorAssignment_0() {
            return cGeneratorAssignment_0;
        }

        public CrossReference getGeneratorGeneratorCrossReference_0_0() {
            return cGeneratorGeneratorCrossReference_0_0;
        }

        public RuleCall getGeneratorGeneratorSTRINGTerminalRuleCall_0_0_1() {
            return cGeneratorGeneratorSTRINGTerminalRuleCall_0_0_1;
        }

        public Assignment getShapeAssignment_1() {
            return cShapeAssignment_1;
        }

        public CrossReference getShapeShapeCrossReference_1_0() {
            return cShapeShapeCrossReference_1_0;
        }

        public RuleCall getShapeShapeSTRINGTerminalRuleCall_1_0_1() {
            return cShapeShapeSTRINGTerminalRuleCall_1_0_1;
        }

        public Keyword getColonEqualsSignKeyword_2() {
            return cColonEqualsSignKeyword_2;
        }

        public Keyword getHyphenMinusKeyword_3() {
            return cHyphenMinusKeyword_3;
        }

        public Assignment getValueAssignment_4() {
            return cValueAssignment_4;
        }

        public RuleCall getValueNumberParserRuleCall_4_0() {
            return cValueNumberParserRuleCall_4_0;
        }
    }

    public class ShapeElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Shape");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cNameAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cNameSTRINGTerminalRuleCall_0_0 = (RuleCall) cNameAssignment_0.eContents().get(0);

        private final Assignment cAgentsAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cAgentsExpressionParserRuleCall_1_0 = (RuleCall) cAgentsAssignment_1.eContents().get(0);

        private final Keyword cColonEqualsSignKeyword_2 = (Keyword) cGroup.eContents().get(2);

        private final Keyword cHyphenMinusKeyword_3 = (Keyword) cGroup.eContents().get(3);

        private final Assignment cValueAssignment_4 = (Assignment) cGroup.eContents().get(4);

        private final RuleCall cValueNumberParserRuleCall_4_0 = (RuleCall) cValueAssignment_4.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getNameAssignment_0() {
            return cNameAssignment_0;
        }

        public RuleCall getNameSTRINGTerminalRuleCall_0_0() {
            return cNameSTRINGTerminalRuleCall_0_0;
        }

        public Assignment getAgentsAssignment_1() {
            return cAgentsAssignment_1;
        }

        public RuleCall getAgentsExpressionParserRuleCall_1_0() {
            return cAgentsExpressionParserRuleCall_1_0;
        }

        public Keyword getColonEqualsSignKeyword_2() {
            return cColonEqualsSignKeyword_2;
        }

        public Keyword getHyphenMinusKeyword_3() {
            return cHyphenMinusKeyword_3;
        }

        public Assignment getValueAssignment_4() {
            return cValueAssignment_4;
        }

        public RuleCall getValueNumberParserRuleCall_4_0() {
            return cValueNumberParserRuleCall_4_0;
        }
    }

    public class GeneratorElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Generator");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cNameAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cNameSTRINGTerminalRuleCall_0_0 = (RuleCall) cNameAssignment_0.eContents().get(0);

        private final Assignment cLhsAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cLhsExpressionParserRuleCall_1_0 = (RuleCall) cLhsAssignment_1.eContents().get(0);

        private final Keyword cLessThanSignHyphenMinusGreaterThanSignKeyword_2 = (Keyword) cGroup.eContents().get(2);

        private final Assignment cRhsAssignment_3 = (Assignment) cGroup.eContents().get(3);

        private final RuleCall cRhsExpressionParserRuleCall_3_0 = (RuleCall) cRhsAssignment_3.eContents().get(0);

        private final Keyword cColonEqualsSignKeyword_4 = (Keyword) cGroup.eContents().get(4);

        private final Keyword cHyphenMinusKeyword_5 = (Keyword) cGroup.eContents().get(5);

        private final Assignment cValueAssignment_6 = (Assignment) cGroup.eContents().get(6);

        private final RuleCall cValueNumberParserRuleCall_6_0 = (RuleCall) cValueAssignment_6.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getNameAssignment_0() {
            return cNameAssignment_0;
        }

        public RuleCall getNameSTRINGTerminalRuleCall_0_0() {
            return cNameSTRINGTerminalRuleCall_0_0;
        }

        public Assignment getLhsAssignment_1() {
            return cLhsAssignment_1;
        }

        public RuleCall getLhsExpressionParserRuleCall_1_0() {
            return cLhsExpressionParserRuleCall_1_0;
        }

        public Keyword getLessThanSignHyphenMinusGreaterThanSignKeyword_2() {
            return cLessThanSignHyphenMinusGreaterThanSignKeyword_2;
        }

        public Assignment getRhsAssignment_3() {
            return cRhsAssignment_3;
        }

        public RuleCall getRhsExpressionParserRuleCall_3_0() {
            return cRhsExpressionParserRuleCall_3_0;
        }

        public Keyword getColonEqualsSignKeyword_4() {
            return cColonEqualsSignKeyword_4;
        }

        public Keyword getHyphenMinusKeyword_5() {
            return cHyphenMinusKeyword_5;
        }

        public Assignment getValueAssignment_6() {
            return cValueAssignment_6;
        }

        public RuleCall getValueNumberParserRuleCall_6_0() {
            return cValueNumberParserRuleCall_6_0;
        }
    }

    public class CommentLineElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "CommentLine");

        private final Assignment cCommentAssignment = (Assignment) rule.eContents().get(1);

        private final RuleCall cCommentSL_COMMENTTerminalRuleCall_0 = (RuleCall) cCommentAssignment.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Assignment getCommentAssignment() {
            return cCommentAssignment;
        }

        public RuleCall getCommentSL_COMMENTTerminalRuleCall_0() {
            return cCommentSL_COMMENTTerminalRuleCall_0;
        }
    }

    public class InitialiseElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Initialise");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Group cGroup_0 = (Group) cGroup.eContents().get(0);

        private final Assignment cMultipleAssignment_0_0 = (Assignment) cGroup_0.eContents().get(0);

        private final RuleCall cMultipleINTTerminalRuleCall_0_0_0 = (RuleCall) cMultipleAssignment_0_0.eContents().get(0);

        private final RuleCall cMULTIPLYTerminalRuleCall_0_1 = (RuleCall) cGroup_0.eContents().get(1);

        private final Assignment cAgentsAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cAgentsExpressionParserRuleCall_1_0 = (RuleCall) cAgentsAssignment_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Group getGroup_0() {
            return cGroup_0;
        }

        public Assignment getMultipleAssignment_0_0() {
            return cMultipleAssignment_0_0;
        }

        public RuleCall getMultipleINTTerminalRuleCall_0_0_0() {
            return cMultipleINTTerminalRuleCall_0_0_0;
        }

        public RuleCall getMULTIPLYTerminalRuleCall_0_1() {
            return cMULTIPLYTerminalRuleCall_0_1;
        }

        public Assignment getAgentsAssignment_1() {
            return cAgentsAssignment_1;
        }

        public RuleCall getAgentsExpressionParserRuleCall_1_0() {
            return cAgentsExpressionParserRuleCall_1_0;
        }
    }

    public class RuleElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Rule");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cNameAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cNameSTRINGTerminalRuleCall_0_0 = (RuleCall) cNameAssignment_0.eContents().get(0);

        private final Assignment cLhsAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final RuleCall cLhsExpressionParserRuleCall_1_0 = (RuleCall) cLhsAssignment_1.eContents().get(0);

        private final Assignment cRhsAssignment_2 = (Assignment) cGroup.eContents().get(2);

        private final RuleCall cRhsRuleRhsParserRuleCall_2_0 = (RuleCall) cRhsAssignment_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getNameAssignment_0() {
            return cNameAssignment_0;
        }

        public RuleCall getNameSTRINGTerminalRuleCall_0_0() {
            return cNameSTRINGTerminalRuleCall_0_0;
        }

        public Assignment getLhsAssignment_1() {
            return cLhsAssignment_1;
        }

        public RuleCall getLhsExpressionParserRuleCall_1_0() {
            return cLhsExpressionParserRuleCall_1_0;
        }

        public Assignment getRhsAssignment_2() {
            return cRhsAssignment_2;
        }

        public RuleCall getRhsRuleRhsParserRuleCall_2_0() {
            return cRhsRuleRhsParserRuleCall_2_0;
        }
    }

    public class RuleRhsElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "RuleRhs");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Group cGroup_0 = (Group) cAlternatives.eContents().get(0);

        private final Assignment cDirectionAssignment_0_0 = (Assignment) cGroup_0.eContents().get(0);

        private final Keyword cDirectionHyphenMinusGreaterThanSignKeyword_0_0_0 = (Keyword) cDirectionAssignment_0_0.eContents().get(0);

        private final Assignment cExpAssignment_0_1 = (Assignment) cGroup_0.eContents().get(1);

        private final RuleCall cExpExpressionParserRuleCall_0_1_0 = (RuleCall) cExpAssignment_0_1.eContents().get(0);

        private final Keyword cCommercialAtKeyword_0_2 = (Keyword) cGroup_0.eContents().get(2);

        private final Assignment cRate1Assignment_0_3 = (Assignment) cGroup_0.eContents().get(3);

        private final RuleCall cRate1NumberParserRuleCall_0_3_0 = (RuleCall) cRate1Assignment_0_3.eContents().get(0);

        private final Group cGroup_1 = (Group) cAlternatives.eContents().get(1);

        private final Assignment cDirectionAssignment_1_0 = (Assignment) cGroup_1.eContents().get(0);

        private final Keyword cDirectionLessThanSignHyphenMinusGreaterThanSignKeyword_1_0_0 = (Keyword) cDirectionAssignment_1_0.eContents().get(0);

        private final Assignment cExpAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cExpExpressionParserRuleCall_1_1_0 = (RuleCall) cExpAssignment_1_1.eContents().get(0);

        private final Keyword cCommercialAtKeyword_1_2 = (Keyword) cGroup_1.eContents().get(2);

        private final Assignment cRate1Assignment_1_3 = (Assignment) cGroup_1.eContents().get(3);

        private final RuleCall cRate1NumberParserRuleCall_1_3_0 = (RuleCall) cRate1Assignment_1_3.eContents().get(0);

        private final Keyword cCommaKeyword_1_4 = (Keyword) cGroup_1.eContents().get(4);

        private final Assignment cRate2Assignment_1_5 = (Assignment) cGroup_1.eContents().get(5);

        private final RuleCall cRate2NumberParserRuleCall_1_5_0 = (RuleCall) cRate2Assignment_1_5.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Group getGroup_0() {
            return cGroup_0;
        }

        public Assignment getDirectionAssignment_0_0() {
            return cDirectionAssignment_0_0;
        }

        public Keyword getDirectionHyphenMinusGreaterThanSignKeyword_0_0_0() {
            return cDirectionHyphenMinusGreaterThanSignKeyword_0_0_0;
        }

        public Assignment getExpAssignment_0_1() {
            return cExpAssignment_0_1;
        }

        public RuleCall getExpExpressionParserRuleCall_0_1_0() {
            return cExpExpressionParserRuleCall_0_1_0;
        }

        public Keyword getCommercialAtKeyword_0_2() {
            return cCommercialAtKeyword_0_2;
        }

        public Assignment getRate1Assignment_0_3() {
            return cRate1Assignment_0_3;
        }

        public RuleCall getRate1NumberParserRuleCall_0_3_0() {
            return cRate1NumberParserRuleCall_0_3_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Assignment getDirectionAssignment_1_0() {
            return cDirectionAssignment_1_0;
        }

        public Keyword getDirectionLessThanSignHyphenMinusGreaterThanSignKeyword_1_0_0() {
            return cDirectionLessThanSignHyphenMinusGreaterThanSignKeyword_1_0_0;
        }

        public Assignment getExpAssignment_1_1() {
            return cExpAssignment_1_1;
        }

        public RuleCall getExpExpressionParserRuleCall_1_1_0() {
            return cExpExpressionParserRuleCall_1_1_0;
        }

        public Keyword getCommercialAtKeyword_1_2() {
            return cCommercialAtKeyword_1_2;
        }

        public Assignment getRate1Assignment_1_3() {
            return cRate1Assignment_1_3;
        }

        public RuleCall getRate1NumberParserRuleCall_1_3_0() {
            return cRate1NumberParserRuleCall_1_3_0;
        }

        public Keyword getCommaKeyword_1_4() {
            return cCommaKeyword_1_4;
        }

        public Assignment getRate2Assignment_1_5() {
            return cRate2Assignment_1_5;
        }

        public RuleCall getRate2NumberParserRuleCall_1_5_0() {
            return cRate2NumberParserRuleCall_1_5_0;
        }
    }

    public class ExpressionElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Expression");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cAgentsAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cAgentsAgentParserRuleCall_0_0 = (RuleCall) cAgentsAssignment_0.eContents().get(0);

        private final Group cGroup_1 = (Group) cGroup.eContents().get(1);

        private final Keyword cCommaKeyword_1_0 = (Keyword) cGroup_1.eContents().get(0);

        private final Assignment cAgentsAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cAgentsAgentParserRuleCall_1_1_0 = (RuleCall) cAgentsAssignment_1_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getAgentsAssignment_0() {
            return cAgentsAssignment_0;
        }

        public RuleCall getAgentsAgentParserRuleCall_0_0() {
            return cAgentsAgentParserRuleCall_0_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Keyword getCommaKeyword_1_0() {
            return cCommaKeyword_1_0;
        }

        public Assignment getAgentsAssignment_1_1() {
            return cAgentsAssignment_1_1;
        }

        public RuleCall getAgentsAgentParserRuleCall_1_1_0() {
            return cAgentsAgentParserRuleCall_1_1_0;
        }
    }

    public class AgentElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Agent");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cIdAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cIdIDTerminalRuleCall_0_0 = (RuleCall) cIdAssignment_0.eContents().get(0);

        private final Keyword cLeftParenthesisKeyword_1 = (Keyword) cGroup.eContents().get(1);

        private final Group cGroup_2 = (Group) cGroup.eContents().get(2);

        private final Assignment cSitesAssignment_2_0 = (Assignment) cGroup_2.eContents().get(0);

        private final RuleCall cSitesSiteParserRuleCall_2_0_0 = (RuleCall) cSitesAssignment_2_0.eContents().get(0);

        private final Group cGroup_2_1 = (Group) cGroup_2.eContents().get(1);

        private final Keyword cCommaKeyword_2_1_0 = (Keyword) cGroup_2_1.eContents().get(0);

        private final Assignment cSitesAssignment_2_1_1 = (Assignment) cGroup_2_1.eContents().get(1);

        private final RuleCall cSitesSiteParserRuleCall_2_1_1_0 = (RuleCall) cSitesAssignment_2_1_1.eContents().get(0);

        private final Keyword cRightParenthesisKeyword_3 = (Keyword) cGroup.eContents().get(3);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getIdAssignment_0() {
            return cIdAssignment_0;
        }

        public RuleCall getIdIDTerminalRuleCall_0_0() {
            return cIdIDTerminalRuleCall_0_0;
        }

        public Keyword getLeftParenthesisKeyword_1() {
            return cLeftParenthesisKeyword_1;
        }

        public Group getGroup_2() {
            return cGroup_2;
        }

        public Assignment getSitesAssignment_2_0() {
            return cSitesAssignment_2_0;
        }

        public RuleCall getSitesSiteParserRuleCall_2_0_0() {
            return cSitesSiteParserRuleCall_2_0_0;
        }

        public Group getGroup_2_1() {
            return cGroup_2_1;
        }

        public Keyword getCommaKeyword_2_1_0() {
            return cCommaKeyword_2_1_0;
        }

        public Assignment getSitesAssignment_2_1_1() {
            return cSitesAssignment_2_1_1;
        }

        public RuleCall getSitesSiteParserRuleCall_2_1_1_0() {
            return cSitesSiteParserRuleCall_2_1_1_0;
        }

        public Keyword getRightParenthesisKeyword_3() {
            return cRightParenthesisKeyword_3;
        }
    }

    public class SiteElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Site");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cIdAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cIdIDTerminalRuleCall_0_0 = (RuleCall) cIdAssignment_0.eContents().get(0);

        private final Group cGroup_1 = (Group) cGroup.eContents().get(1);

        private final Keyword cTildeKeyword_1_0 = (Keyword) cGroup_1.eContents().get(0);

        private final Assignment cStateAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cStateMrkParserRuleCall_1_1_0 = (RuleCall) cStateAssignment_1_1.eContents().get(0);

        private final Assignment cLinkAssignment_2 = (Assignment) cGroup.eContents().get(2);

        private final RuleCall cLinkLinkParserRuleCall_2_0 = (RuleCall) cLinkAssignment_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getIdAssignment_0() {
            return cIdAssignment_0;
        }

        public RuleCall getIdIDTerminalRuleCall_0_0() {
            return cIdIDTerminalRuleCall_0_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Keyword getTildeKeyword_1_0() {
            return cTildeKeyword_1_0;
        }

        public Assignment getStateAssignment_1_1() {
            return cStateAssignment_1_1;
        }

        public RuleCall getStateMrkParserRuleCall_1_1_0() {
            return cStateMrkParserRuleCall_1_1_0;
        }

        public Assignment getLinkAssignment_2() {
            return cLinkAssignment_2;
        }

        public RuleCall getLinkLinkParserRuleCall_2_0() {
            return cLinkLinkParserRuleCall_2_0;
        }
    }

    public class MrkElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Mrk");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Assignment cNAssignment_0 = (Assignment) cAlternatives.eContents().get(0);

        private final RuleCall cNINTTerminalRuleCall_0_0 = (RuleCall) cNAssignment_0.eContents().get(0);

        private final Assignment cIdAssignment_1 = (Assignment) cAlternatives.eContents().get(1);

        private final RuleCall cIdIDTerminalRuleCall_1_0 = (RuleCall) cIdAssignment_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Assignment getNAssignment_0() {
            return cNAssignment_0;
        }

        public RuleCall getNINTTerminalRuleCall_0_0() {
            return cNINTTerminalRuleCall_0_0;
        }

        public Assignment getIdAssignment_1() {
            return cIdAssignment_1;
        }

        public RuleCall getIdIDTerminalRuleCall_1_0() {
            return cIdIDTerminalRuleCall_1_0;
        }
    }

    public class LinkElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Link");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Group cGroup_0 = (Group) cAlternatives.eContents().get(0);

        private final Keyword cExclamationMarkKeyword_0_0 = (Keyword) cGroup_0.eContents().get(0);

        private final Alternatives cAlternatives_0_1 = (Alternatives) cGroup_0.eContents().get(1);

        private final Assignment cNAssignment_0_1_0 = (Assignment) cAlternatives_0_1.eContents().get(0);

        private final RuleCall cNINTTerminalRuleCall_0_1_0_0 = (RuleCall) cNAssignment_0_1_0.eContents().get(0);

        private final Assignment cCAssignment_0_1_1 = (Assignment) cAlternatives_0_1.eContents().get(1);

        private final Keyword cC_Keyword_0_1_1_0 = (Keyword) cCAssignment_0_1_1.eContents().get(0);

        private final Assignment cCAssignment_1 = (Assignment) cAlternatives.eContents().get(1);

        private final Keyword cCQuestionMarkKeyword_1_0 = (Keyword) cCAssignment_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Group getGroup_0() {
            return cGroup_0;
        }

        public Keyword getExclamationMarkKeyword_0_0() {
            return cExclamationMarkKeyword_0_0;
        }

        public Alternatives getAlternatives_0_1() {
            return cAlternatives_0_1;
        }

        public Assignment getNAssignment_0_1_0() {
            return cNAssignment_0_1_0;
        }

        public RuleCall getNINTTerminalRuleCall_0_1_0_0() {
            return cNINTTerminalRuleCall_0_1_0_0;
        }

        public Assignment getCAssignment_0_1_1() {
            return cCAssignment_0_1_1;
        }

        public Keyword getC_Keyword_0_1_1_0() {
            return cC_Keyword_0_1_1_0;
        }

        public Assignment getCAssignment_1() {
            return cCAssignment_1;
        }

        public Keyword getCQuestionMarkKeyword_1_0() {
            return cCQuestionMarkKeyword_1_0;
        }
    }

    public class ObserveElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Observe");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Assignment cLabelAssignment_0 = (Assignment) cAlternatives.eContents().get(0);

        private final RuleCall cLabelSTRINGTerminalRuleCall_0_0 = (RuleCall) cLabelAssignment_0.eContents().get(0);

        private final Group cGroup_1 = (Group) cAlternatives.eContents().get(1);

        private final Assignment cLabelAssignment_1_0 = (Assignment) cGroup_1.eContents().get(0);

        private final RuleCall cLabelSTRINGTerminalRuleCall_1_0_0 = (RuleCall) cLabelAssignment_1_0.eContents().get(0);

        private final Assignment cExpressionAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cExpressionExpressionParserRuleCall_1_1_0 = (RuleCall) cExpressionAssignment_1_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Assignment getLabelAssignment_0() {
            return cLabelAssignment_0;
        }

        public RuleCall getLabelSTRINGTerminalRuleCall_0_0() {
            return cLabelSTRINGTerminalRuleCall_0_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Assignment getLabelAssignment_1_0() {
            return cLabelAssignment_1_0;
        }

        public RuleCall getLabelSTRINGTerminalRuleCall_1_0_0() {
            return cLabelSTRINGTerminalRuleCall_1_0_0;
        }

        public Assignment getExpressionAssignment_1_1() {
            return cExpressionAssignment_1_1;
        }

        public RuleCall getExpressionExpressionParserRuleCall_1_1_0() {
            return cExpressionExpressionParserRuleCall_1_1_0;
        }
    }

    public class StoryElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Story");

        private final Assignment cStoryAssignment = (Assignment) rule.eContents().get(1);

        private final RuleCall cStorySTRINGTerminalRuleCall_0 = (RuleCall) cStoryAssignment.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Assignment getStoryAssignment() {
            return cStoryAssignment;
        }

        public RuleCall getStorySTRINGTerminalRuleCall_0() {
            return cStorySTRINGTerminalRuleCall_0;
        }
    }

    public class ModificationElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Modification");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Group cGroup_0 = (Group) cAlternatives.eContents().get(0);

        private final Keyword cTKeyword_0_0 = (Keyword) cGroup_0.eContents().get(0);

        private final Assignment cTimeAssignment_0_1 = (Assignment) cGroup_0.eContents().get(1);

        private final RuleCall cTimeNumberParserRuleCall_0_1_0 = (RuleCall) cTimeAssignment_0_1.eContents().get(0);

        private final Keyword cDoKeyword_0_2 = (Keyword) cGroup_0.eContents().get(2);

        private final Assignment cAAssignment_0_3 = (Assignment) cGroup_0.eContents().get(3);

        private final RuleCall cAAssignmentParserRuleCall_0_3_0 = (RuleCall) cAAssignment_0_3.eContents().get(0);

        private final Group cGroup_1 = (Group) cAlternatives.eContents().get(1);

        private final Assignment cConcentrationTestAssignment_1_0 = (Assignment) cGroup_1.eContents().get(0);

        private final RuleCall cConcentrationTestConcentrationParserRuleCall_1_0_0 = (RuleCall) cConcentrationTestAssignment_1_0.eContents().get(0);

        private final Keyword cDoKeyword_1_1 = (Keyword) cGroup_1.eContents().get(1);

        private final Assignment cAAssignment_1_2 = (Assignment) cGroup_1.eContents().get(2);

        private final RuleCall cAAssignmentParserRuleCall_1_2_0 = (RuleCall) cAAssignment_1_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Group getGroup_0() {
            return cGroup_0;
        }

        public Keyword getTKeyword_0_0() {
            return cTKeyword_0_0;
        }

        public Assignment getTimeAssignment_0_1() {
            return cTimeAssignment_0_1;
        }

        public RuleCall getTimeNumberParserRuleCall_0_1_0() {
            return cTimeNumberParserRuleCall_0_1_0;
        }

        public Keyword getDoKeyword_0_2() {
            return cDoKeyword_0_2;
        }

        public Assignment getAAssignment_0_3() {
            return cAAssignment_0_3;
        }

        public RuleCall getAAssignmentParserRuleCall_0_3_0() {
            return cAAssignmentParserRuleCall_0_3_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Assignment getConcentrationTestAssignment_1_0() {
            return cConcentrationTestAssignment_1_0;
        }

        public RuleCall getConcentrationTestConcentrationParserRuleCall_1_0_0() {
            return cConcentrationTestConcentrationParserRuleCall_1_0_0;
        }

        public Keyword getDoKeyword_1_1() {
            return cDoKeyword_1_1;
        }

        public Assignment getAAssignment_1_2() {
            return cAAssignment_1_2;
        }

        public RuleCall getAAssignmentParserRuleCall_1_2_0() {
            return cAAssignmentParserRuleCall_1_2_0;
        }
    }

    public class ConcentrationElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Concentration");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cLhsAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cLhsValueCalcParserRuleCall_0_0 = (RuleCall) cLhsAssignment_0.eContents().get(0);

        private final Assignment cOpAssignment_1 = (Assignment) cGroup.eContents().get(1);

        private final Alternatives cOpAlternatives_1_0 = (Alternatives) cOpAssignment_1.eContents().get(0);

        private final Keyword cOpLessThanSignKeyword_1_0_0 = (Keyword) cOpAlternatives_1_0.eContents().get(0);

        private final Keyword cOpGreaterThanSignKeyword_1_0_1 = (Keyword) cOpAlternatives_1_0.eContents().get(1);

        private final Assignment cRhsAssignment_2 = (Assignment) cGroup.eContents().get(2);

        private final RuleCall cRhsValueCalcParserRuleCall_2_0 = (RuleCall) cRhsAssignment_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getLhsAssignment_0() {
            return cLhsAssignment_0;
        }

        public RuleCall getLhsValueCalcParserRuleCall_0_0() {
            return cLhsValueCalcParserRuleCall_0_0;
        }

        public Assignment getOpAssignment_1() {
            return cOpAssignment_1;
        }

        public Alternatives getOpAlternatives_1_0() {
            return cOpAlternatives_1_0;
        }

        public Keyword getOpLessThanSignKeyword_1_0_0() {
            return cOpLessThanSignKeyword_1_0_0;
        }

        public Keyword getOpGreaterThanSignKeyword_1_0_1() {
            return cOpGreaterThanSignKeyword_1_0_1;
        }

        public Assignment getRhsAssignment_2() {
            return cRhsAssignment_2;
        }

        public RuleCall getRhsValueCalcParserRuleCall_2_0() {
            return cRhsValueCalcParserRuleCall_2_0;
        }
    }

    public class AssignmentElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Assignment");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final Assignment cNameAssignment_0 = (Assignment) cGroup.eContents().get(0);

        private final RuleCall cNameSTRINGTerminalRuleCall_0_0 = (RuleCall) cNameAssignment_0.eContents().get(0);

        private final Keyword cColonEqualsSignKeyword_1 = (Keyword) cGroup.eContents().get(1);

        private final Assignment cValueAssignment_2 = (Assignment) cGroup.eContents().get(2);

        private final RuleCall cValueValueCalcParserRuleCall_2_0 = (RuleCall) cValueAssignment_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public Assignment getNameAssignment_0() {
            return cNameAssignment_0;
        }

        public RuleCall getNameSTRINGTerminalRuleCall_0_0() {
            return cNameSTRINGTerminalRuleCall_0_0;
        }

        public Keyword getColonEqualsSignKeyword_1() {
            return cColonEqualsSignKeyword_1;
        }

        public Assignment getValueAssignment_2() {
            return cValueAssignment_2;
        }

        public RuleCall getValueValueCalcParserRuleCall_2_0() {
            return cValueValueCalcParserRuleCall_2_0;
        }
    }

    public class ValueCalcElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "ValueCalc");

        private final Group cGroup = (Group) rule.eContents().get(1);

        private final RuleCall cAtomParserRuleCall_0 = (RuleCall) cGroup.eContents().get(0);

        private final Group cGroup_1 = (Group) cGroup.eContents().get(1);

        private final Assignment cOpAssignment_1_0 = (Assignment) cGroup_1.eContents().get(0);

        private final RuleCall cOpOpParserRuleCall_1_0_0 = (RuleCall) cOpAssignment_1_0.eContents().get(0);

        private final Assignment cRhsAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cRhsValueCalcParserRuleCall_1_1_0 = (RuleCall) cRhsAssignment_1_1.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Group getGroup() {
            return cGroup;
        }

        public RuleCall getAtomParserRuleCall_0() {
            return cAtomParserRuleCall_0;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Assignment getOpAssignment_1_0() {
            return cOpAssignment_1_0;
        }

        public RuleCall getOpOpParserRuleCall_1_0_0() {
            return cOpOpParserRuleCall_1_0_0;
        }

        public Assignment getRhsAssignment_1_1() {
            return cRhsAssignment_1_1;
        }

        public RuleCall getRhsValueCalcParserRuleCall_1_1_0() {
            return cRhsValueCalcParserRuleCall_1_1_0;
        }
    }

    public class AtomElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Atom");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Group cGroup_0 = (Group) cAlternatives.eContents().get(0);

        private final Keyword cLeftParenthesisKeyword_0_0 = (Keyword) cGroup_0.eContents().get(0);

        private final Assignment cExpAssignment_0_1 = (Assignment) cGroup_0.eContents().get(1);

        private final RuleCall cExpValueCalcParserRuleCall_0_1_0 = (RuleCall) cExpAssignment_0_1.eContents().get(0);

        private final Keyword cRightParenthesisKeyword_0_2 = (Keyword) cGroup_0.eContents().get(2);

        private final Group cGroup_1 = (Group) cAlternatives.eContents().get(1);

        private final Keyword cLeftSquareBracketKeyword_1_0 = (Keyword) cGroup_1.eContents().get(0);

        private final Assignment cLabelAssignment_1_1 = (Assignment) cGroup_1.eContents().get(1);

        private final RuleCall cLabelSTRINGTerminalRuleCall_1_1_0 = (RuleCall) cLabelAssignment_1_1.eContents().get(0);

        private final Keyword cRightSquareBracketKeyword_1_2 = (Keyword) cGroup_1.eContents().get(2);

        private final Assignment cNumberAssignment_2 = (Assignment) cAlternatives.eContents().get(2);

        private final RuleCall cNumberNumberParserRuleCall_2_0 = (RuleCall) cNumberAssignment_2.eContents().get(0);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Group getGroup_0() {
            return cGroup_0;
        }

        public Keyword getLeftParenthesisKeyword_0_0() {
            return cLeftParenthesisKeyword_0_0;
        }

        public Assignment getExpAssignment_0_1() {
            return cExpAssignment_0_1;
        }

        public RuleCall getExpValueCalcParserRuleCall_0_1_0() {
            return cExpValueCalcParserRuleCall_0_1_0;
        }

        public Keyword getRightParenthesisKeyword_0_2() {
            return cRightParenthesisKeyword_0_2;
        }

        public Group getGroup_1() {
            return cGroup_1;
        }

        public Keyword getLeftSquareBracketKeyword_1_0() {
            return cLeftSquareBracketKeyword_1_0;
        }

        public Assignment getLabelAssignment_1_1() {
            return cLabelAssignment_1_1;
        }

        public RuleCall getLabelSTRINGTerminalRuleCall_1_1_0() {
            return cLabelSTRINGTerminalRuleCall_1_1_0;
        }

        public Keyword getRightSquareBracketKeyword_1_2() {
            return cRightSquareBracketKeyword_1_2;
        }

        public Assignment getNumberAssignment_2() {
            return cNumberAssignment_2;
        }

        public RuleCall getNumberNumberParserRuleCall_2_0() {
            return cNumberNumberParserRuleCall_2_0;
        }
    }

    public class NumberElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Number");

        private final Alternatives cAlternatives = (Alternatives) rule.eContents().get(1);

        private final Assignment cNintAssignment_0 = (Assignment) cAlternatives.eContents().get(0);

        private final RuleCall cNintINTTerminalRuleCall_0_0 = (RuleCall) cNintAssignment_0.eContents().get(0);

        private final Assignment cNdecimalAssignment_1 = (Assignment) cAlternatives.eContents().get(1);

        private final RuleCall cNdecimalDECIMALTerminalRuleCall_1_0 = (RuleCall) cNdecimalAssignment_1.eContents().get(0);

        private final Group cGroup_2 = (Group) cAlternatives.eContents().get(2);

        private final Keyword cLnKeyword_2_0 = (Keyword) cGroup_2.eContents().get(0);

        private final Assignment cNumberAssignment_2_1 = (Assignment) cGroup_2.eContents().get(1);

        private final RuleCall cNumberNumberParserRuleCall_2_1_0 = (RuleCall) cNumberAssignment_2_1.eContents().get(0);

        private final Keyword cRightParenthesisKeyword_2_2 = (Keyword) cGroup_2.eContents().get(2);

        public ParserRule getRule() {
            return rule;
        }

        public Alternatives getAlternatives() {
            return cAlternatives;
        }

        public Assignment getNintAssignment_0() {
            return cNintAssignment_0;
        }

        public RuleCall getNintINTTerminalRuleCall_0_0() {
            return cNintINTTerminalRuleCall_0_0;
        }

        public Assignment getNdecimalAssignment_1() {
            return cNdecimalAssignment_1;
        }

        public RuleCall getNdecimalDECIMALTerminalRuleCall_1_0() {
            return cNdecimalDECIMALTerminalRuleCall_1_0;
        }

        public Group getGroup_2() {
            return cGroup_2;
        }

        public Keyword getLnKeyword_2_0() {
            return cLnKeyword_2_0;
        }

        public Assignment getNumberAssignment_2_1() {
            return cNumberAssignment_2_1;
        }

        public RuleCall getNumberNumberParserRuleCall_2_1_0() {
            return cNumberNumberParserRuleCall_2_1_0;
        }

        public Keyword getRightParenthesisKeyword_2_2() {
            return cRightParenthesisKeyword_2_2;
        }
    }

    public class OpElements extends AbstractParserRuleElementFinder {

        private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Op");

        private final Assignment cOpAssignment = (Assignment) rule.eContents().get(1);

        private final Alternatives cOpAlternatives_0 = (Alternatives) cOpAssignment.eContents().get(0);

        private final Keyword cOpPlusSignKeyword_0_0 = (Keyword) cOpAlternatives_0.eContents().get(0);

        private final Keyword cOpHyphenMinusKeyword_0_1 = (Keyword) cOpAlternatives_0.eContents().get(1);

        private final Keyword cOpSolidusKeyword_0_2 = (Keyword) cOpAlternatives_0.eContents().get(2);

        private final RuleCall cOpMULTIPLYTerminalRuleCall_0_3 = (RuleCall) cOpAlternatives_0.eContents().get(3);

        public ParserRule getRule() {
            return rule;
        }

        public Assignment getOpAssignment() {
            return cOpAssignment;
        }

        public Alternatives getOpAlternatives_0() {
            return cOpAlternatives_0;
        }

        public Keyword getOpPlusSignKeyword_0_0() {
            return cOpPlusSignKeyword_0_0;
        }

        public Keyword getOpHyphenMinusKeyword_0_1() {
            return cOpHyphenMinusKeyword_0_1;
        }

        public Keyword getOpSolidusKeyword_0_2() {
            return cOpSolidusKeyword_0_2;
        }

        public RuleCall getOpMULTIPLYTerminalRuleCall_0_3() {
            return cOpMULTIPLYTerminalRuleCall_0_3;
        }
    }

    private ModelElements pModel;

    private LineElements pLine;

    private ImportsElements pImports;

    private LambdaElements pLambda;

    private ShapeElements pShape;

    private GeneratorElements pGenerator;

    private CommentLineElements pCommentLine;

    private InitialiseElements pInitialise;

    private RuleElements pRule;

    private RuleRhsElements pRuleRhs;

    private ExpressionElements pExpression;

    private AgentElements pAgent;

    private SiteElements pSite;

    private MrkElements pMrk;

    private LinkElements pLink;

    private ObserveElements pObserve;

    private StoryElements pStory;

    private ModificationElements pModification;

    private ConcentrationElements pConcentration;

    private AssignmentElements pAssignment;

    private ValueCalcElements pValueCalc;

    private AtomElements pAtom;

    private NumberElements pNumber;

    private OpElements pOp;

    private TerminalRule tSL_COMMENT;

    private TerminalRule tID;

    private TerminalRule tINT;

    private TerminalRule tDECIMAL;

    private TerminalRule tSTRING;

    private TerminalRule tWS;

    private TerminalRule tEOL;

    private TerminalRule tMULTIPLY;

    private TerminalRule tANY_OTHER;

    private final GrammarProvider grammarProvider;

    @Inject
    public UrbanGrammarAccess(GrammarProvider grammarProvider) {
        this.grammarProvider = grammarProvider;
    }

    public Grammar getGrammar() {
        return grammarProvider.getGrammar(this);
    }

    public ModelElements getModelAccess() {
        return (pModel != null) ? pModel : (pModel = new ModelElements());
    }

    public ParserRule getModelRule() {
        return getModelAccess().getRule();
    }

    public LineElements getLineAccess() {
        return (pLine != null) ? pLine : (pLine = new LineElements());
    }

    public ParserRule getLineRule() {
        return getLineAccess().getRule();
    }

    public ImportsElements getImportsAccess() {
        return (pImports != null) ? pImports : (pImports = new ImportsElements());
    }

    public ParserRule getImportsRule() {
        return getImportsAccess().getRule();
    }

    public LambdaElements getLambdaAccess() {
        return (pLambda != null) ? pLambda : (pLambda = new LambdaElements());
    }

    public ParserRule getLambdaRule() {
        return getLambdaAccess().getRule();
    }

    public ShapeElements getShapeAccess() {
        return (pShape != null) ? pShape : (pShape = new ShapeElements());
    }

    public ParserRule getShapeRule() {
        return getShapeAccess().getRule();
    }

    public GeneratorElements getGeneratorAccess() {
        return (pGenerator != null) ? pGenerator : (pGenerator = new GeneratorElements());
    }

    public ParserRule getGeneratorRule() {
        return getGeneratorAccess().getRule();
    }

    public CommentLineElements getCommentLineAccess() {
        return (pCommentLine != null) ? pCommentLine : (pCommentLine = new CommentLineElements());
    }

    public ParserRule getCommentLineRule() {
        return getCommentLineAccess().getRule();
    }

    public InitialiseElements getInitialiseAccess() {
        return (pInitialise != null) ? pInitialise : (pInitialise = new InitialiseElements());
    }

    public ParserRule getInitialiseRule() {
        return getInitialiseAccess().getRule();
    }

    public RuleElements getRuleAccess() {
        return (pRule != null) ? pRule : (pRule = new RuleElements());
    }

    public ParserRule getRuleRule() {
        return getRuleAccess().getRule();
    }

    public RuleRhsElements getRuleRhsAccess() {
        return (pRuleRhs != null) ? pRuleRhs : (pRuleRhs = new RuleRhsElements());
    }

    public ParserRule getRuleRhsRule() {
        return getRuleRhsAccess().getRule();
    }

    public ExpressionElements getExpressionAccess() {
        return (pExpression != null) ? pExpression : (pExpression = new ExpressionElements());
    }

    public ParserRule getExpressionRule() {
        return getExpressionAccess().getRule();
    }

    public AgentElements getAgentAccess() {
        return (pAgent != null) ? pAgent : (pAgent = new AgentElements());
    }

    public ParserRule getAgentRule() {
        return getAgentAccess().getRule();
    }

    public SiteElements getSiteAccess() {
        return (pSite != null) ? pSite : (pSite = new SiteElements());
    }

    public ParserRule getSiteRule() {
        return getSiteAccess().getRule();
    }

    public MrkElements getMrkAccess() {
        return (pMrk != null) ? pMrk : (pMrk = new MrkElements());
    }

    public ParserRule getMrkRule() {
        return getMrkAccess().getRule();
    }

    public LinkElements getLinkAccess() {
        return (pLink != null) ? pLink : (pLink = new LinkElements());
    }

    public ParserRule getLinkRule() {
        return getLinkAccess().getRule();
    }

    public ObserveElements getObserveAccess() {
        return (pObserve != null) ? pObserve : (pObserve = new ObserveElements());
    }

    public ParserRule getObserveRule() {
        return getObserveAccess().getRule();
    }

    public StoryElements getStoryAccess() {
        return (pStory != null) ? pStory : (pStory = new StoryElements());
    }

    public ParserRule getStoryRule() {
        return getStoryAccess().getRule();
    }

    public ModificationElements getModificationAccess() {
        return (pModification != null) ? pModification : (pModification = new ModificationElements());
    }

    public ParserRule getModificationRule() {
        return getModificationAccess().getRule();
    }

    public ConcentrationElements getConcentrationAccess() {
        return (pConcentration != null) ? pConcentration : (pConcentration = new ConcentrationElements());
    }

    public ParserRule getConcentrationRule() {
        return getConcentrationAccess().getRule();
    }

    public AssignmentElements getAssignmentAccess() {
        return (pAssignment != null) ? pAssignment : (pAssignment = new AssignmentElements());
    }

    public ParserRule getAssignmentRule() {
        return getAssignmentAccess().getRule();
    }

    public ValueCalcElements getValueCalcAccess() {
        return (pValueCalc != null) ? pValueCalc : (pValueCalc = new ValueCalcElements());
    }

    public ParserRule getValueCalcRule() {
        return getValueCalcAccess().getRule();
    }

    public AtomElements getAtomAccess() {
        return (pAtom != null) ? pAtom : (pAtom = new AtomElements());
    }

    public ParserRule getAtomRule() {
        return getAtomAccess().getRule();
    }

    public NumberElements getNumberAccess() {
        return (pNumber != null) ? pNumber : (pNumber = new NumberElements());
    }

    public ParserRule getNumberRule() {
        return getNumberAccess().getRule();
    }

    public OpElements getOpAccess() {
        return (pOp != null) ? pOp : (pOp = new OpElements());
    }

    public ParserRule getOpRule() {
        return getOpAccess().getRule();
    }

    public TerminalRule getSL_COMMENTRule() {
        return (tSL_COMMENT != null) ? tSL_COMMENT : (tSL_COMMENT = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "SL_COMMENT"));
    }

    public TerminalRule getIDRule() {
        return (tID != null) ? tID : (tID = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "ID"));
    }

    public TerminalRule getINTRule() {
        return (tINT != null) ? tINT : (tINT = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "INT"));
    }

    public TerminalRule getDECIMALRule() {
        return (tDECIMAL != null) ? tDECIMAL : (tDECIMAL = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "DECIMAL"));
    }

    public TerminalRule getSTRINGRule() {
        return (tSTRING != null) ? tSTRING : (tSTRING = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "STRING"));
    }

    public TerminalRule getWSRule() {
        return (tWS != null) ? tWS : (tWS = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "WS"));
    }

    public TerminalRule getEOLRule() {
        return (tEOL != null) ? tEOL : (tEOL = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "EOL"));
    }

    public TerminalRule getMULTIPLYRule() {
        return (tMULTIPLY != null) ? tMULTIPLY : (tMULTIPLY = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "MULTIPLY"));
    }

    public TerminalRule getANY_OTHERRule() {
        return (tANY_OTHER != null) ? tANY_OTHER : (tANY_OTHER = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "ANY_OTHER"));
    }
}
