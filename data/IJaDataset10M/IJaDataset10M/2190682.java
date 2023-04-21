package comp.logo.analysis;

import comp.logo.node.*;

public interface Analysis extends Switch {

    Object getIn(Node node);

    void setIn(Node node, Object o);

    Object getOut(Node node);

    void setOut(Node node, Object o);

    void caseStart(Start node);

    void caseAListProg(AListProg node);

    void caseAListaPInstructionlist(AListaPInstructionlist node);

    void caseAInstPInstructionlist(AInstPInstructionlist node);

    void caseARunPInstructionlist(ARunPInstructionlist node);

    void caseAPrimitivesPInstruction(APrimitivesPInstruction node);

    void caseACommunicationPInstruction(ACommunicationPInstruction node);

    void caseAArithmeticPInstruction(AArithmeticPInstruction node);

    void caseALogicPInstruction(ALogicPInstruction node);

    void caseAGraphicsPInstruction(AGraphicsPInstruction node);

    void caseAWorkspacePInstruction(AWorkspacePInstruction node);

    void caseAControlPInstruction(AControlPInstruction node);

    void caseACalltofunctionPInstruction(ACalltofunctionPInstruction node);

    void caseAVarsPPrimitives(AVarsPPrimitives node);

    void caseAListPPrimitives(AListPPrimitives node);

    void caseASelectorsPPrimitives(ASelectorsPPrimitives node);

    void caseAMutatorPPrimitives(AMutatorPPrimitives node);

    void caseAFirstPSelectors(AFirstPSelectors node);

    void caseAButfirstsPSelectors(AButfirstsPSelectors node);

    void caseALastPSelectors(ALastPSelectors node);

    void caseASetPMutator(ASetPMutator node);

    void caseAPrsPCommunication(APrsPCommunication node);

    void caseASumPArithmetic(ASumPArithmetic node);

    void caseADifferencePArithmetic(ADifferencePArithmetic node);

    void caseAMinusPArithmetic(AMinusPArithmetic node);

    void caseAProductPArithmetic(AProductPArithmetic node);

    void caseAModuloPArithmetic(AModuloPArithmetic node);

    void caseAIntPArithmetic(AIntPArithmetic node);

    void caseARoundPArithmetic(ARoundPArithmetic node);

    void caseASqrtPArithmetic(ASqrtPArithmetic node);

    void caseAPowerPArithmetic(APowerPArithmetic node);

    void caseAExpPArithmetic(AExpPArithmetic node);

    void caseAAndPLogic(AAndPLogic node);

    void caseAOrPLogic(AOrPLogic node);

    void caseANotPLogic(ANotPLogic node);

    void caseAFdsPGraphics(AFdsPGraphics node);

    void caseABksPGraphics(ABksPGraphics node);

    void caseALtsPGraphics(ALtsPGraphics node);

    void caseARtsPGraphics(ARtsPGraphics node);

    void caseASetposPGraphics(ASetposPGraphics node);

    void caseASetxyPGraphics(ASetxyPGraphics node);

    void caseASetxPGraphics(ASetxPGraphics node);

    void caseASetyPGraphics(ASetyPGraphics node);

    void caseASethsPGraphics(ASethsPGraphics node);

    void caseAHomePGraphics(AHomePGraphics node);

    void caseAArcPGraphics(AArcPGraphics node);

    void caseAClrPGraphics(AClrPGraphics node);

    void caseAPenctrlPGraphics(APenctrlPGraphics node);

    void caseAPdsPPencontrol(APdsPPencontrol node);

    void caseAPusPPencontrol(APusPPencontrol node);

    void caseASetpcsPPencontrol(ASetpcsPPencontrol node);

    void caseASetpensizePPencontrol(ASetpensizePPencontrol node);

    void caseAToPWorkspace(AToPWorkspace node);

    void caseAMakesPWorkspace(AMakesPWorkspace node);

    void caseARepeatPControlStructures(ARepeatPControlStructures node);

    void caseAIfPControlStructures(AIfPControlStructures node);

    void caseAIfelsePControlStructures(AIfelsePControlStructures node);

    void caseAStopPControlStructures(AStopPControlStructures node);

    void caseAGotoPControlStructures(AGotoPControlStructures node);

    void caseAPWord(APWord node);

    void caseAPList(APList node);

    void caseAPFirst(APFirst node);

    void caseAButfirstPButfirst(AButfirstPButfirst node);

    void caseABfPButfirst(ABfPButfirst node);

    void caseAPLast(APLast node);

    void caseAPSetitem(APSetitem node);

    void caseAPrintPPrint(APrintPPrint node);

    void caseAPrPPrint(APrPPrint node);

    void caseASumPSum(ASumPSum node);

    void caseAPlusPSum(APlusPSum node);

    void caseADifPDifference(ADifPDifference node);

    void caseAMinPDifference(AMinPDifference node);

    void caseAMinusPMinus(AMinusPMinus node);

    void caseAMinPMinus(AMinPMinus node);

    void caseAProdPProduct(AProdPProduct node);

    void caseAMultPProduct(AMultPProduct node);

    void caseAPModulo(APModulo node);

    void caseAPInt(APInt node);

    void caseAPRound(APRound node);

    void caseAPSqrt(APSqrt node);

    void caseAPPower(APPower node);

    void caseAPExp(APExp node);

    void caseAPAnd(APAnd node);

    void caseAPOr(APOr node);

    void caseAPNot(APNot node);

    void caseAForwardPForward(AForwardPForward node);

    void caseAFdPForward(AFdPForward node);

    void caseABackPBack(ABackPBack node);

    void caseABkPBack(ABkPBack node);

    void caseALeftPLeft(ALeftPLeft node);

    void caseALtPLeft(ALtPLeft node);

    void caseARightPRight(ARightPRight node);

    void caseARtPRight(ARtPRight node);

    void caseAPSetpos(APSetpos node);

    void caseAPSetxy(APSetxy node);

    void caseAPSetx(APSetx node);

    void caseAPSety(APSety node);

    void caseASetheadingPSetheading(ASetheadingPSetheading node);

    void caseASethPSetheading(ASethPSetheading node);

    void caseAPHome(APHome node);

    void caseAPArc(APArc node);

    void caseAClearscreenPClearscreen(AClearscreenPClearscreen node);

    void caseACsPClearscreen(ACsPClearscreen node);

    void caseAPendownPPendown(APendownPPendown node);

    void caseAPdPPendown(APdPPendown node);

    void caseAPenupPPenup(APenupPPenup node);

    void caseAPuPPenup(APuPPenup node);

    void caseASetpencolorPSetpencolor(ASetpencolorPSetpencolor node);

    void caseASetpcPSetpencolor(ASetpcPSetpencolor node);

    void caseASizePSetpensize(ASizePSetpensize node);

    void caseASizexyPSetpensize(ASizexyPSetpensize node);

    void caseAPTo(APTo node);

    void caseAVarwordPMake(AVarwordPMake node);

    void caseAVarnumPMake(AVarnumPMake node);

    void caseAPRepeat(APRepeat node);

    void caseAIfPIf(AIfPIf node);

    void caseAIfelsePIf(AIfelsePIf node);

    void caseAPIfelse(APIfelse node);

    void caseAPStop(APStop node);

    void caseAPGoto(APGoto node);

    void caseAIdentListPIdentList(AIdentListPIdentList node);

    void caseAIdentPIdentList(AIdentPIdentList node);

    void caseAIntPValue(AIntPValue node);

    void caseADobPValue(ADobPValue node);

    void caseAValueListPValueList(AValueListPValueList node);

    void caseAValuePValueList(AValuePValueList node);

    void caseASumValPSumList(ASumValPSumList node);

    void caseASumListPSumList(ASumListPSumList node);

    void caseADifValPDifList(ADifValPDifList node);

    void caseADifListPDifList(ADifListPDifList node);

    void caseAMultValPMultList(AMultValPMultList node);

    void caseAMultListPMultList(AMultListPMultList node);

    void caseATruePBoolean(ATruePBoolean node);

    void caseAFalsePBoolean(AFalsePBoolean node);

    void caseAVarPBoolean(AVarPBoolean node);

    void caseAPredefPColor(APredefPColor node);

    void caseALstPColor(ALstPColor node);

    void caseTProcname(TProcname node);

    void caseTWord(TWord node);

    void caseTList(TList node);

    void caseTFirst(TFirst node);

    void caseTButfirst(TButfirst node);

    void caseTBf(TBf node);

    void caseTLast(TLast node);

    void caseTSetitem(TSetitem node);

    void caseTPush(TPush node);

    void caseTPop(TPop node);

    void caseTPrint(TPrint node);

    void caseTPr(TPr node);

    void caseTSum(TSum node);

    void caseTDifference(TDifference node);

    void caseTMinus(TMinus node);

    void caseTProduct(TProduct node);

    void caseTModulo(TModulo node);

    void caseTInt(TInt node);

    void caseTRound(TRound node);

    void caseTSqrt(TSqrt node);

    void caseTPower(TPower node);

    void caseTExp(TExp node);

    void caseTAnd(TAnd node);

    void caseTOr(TOr node);

    void caseTNot(TNot node);

    void caseTForward(TForward node);

    void caseTFd(TFd node);

    void caseTBack(TBack node);

    void caseTBk(TBk node);

    void caseTLeft(TLeft node);

    void caseTLf(TLf node);

    void caseTRight(TRight node);

    void caseTRt(TRt node);

    void caseTSetpos(TSetpos node);

    void caseTSetxy(TSetxy node);

    void caseTSetx(TSetx node);

    void caseTSety(TSety node);

    void caseTSetheading(TSetheading node);

    void caseTSeth(TSeth node);

    void caseTHome(THome node);

    void caseTArc(TArc node);

    void caseTClearscreen(TClearscreen node);

    void caseTCs(TCs node);

    void caseTPendown(TPendown node);

    void caseTPd(TPd node);

    void caseTPenup(TPenup node);

    void caseTPu(TPu node);

    void caseTSetpencolor(TSetpencolor node);

    void caseTSetpc(TSetpc node);

    void caseTSetpensize(TSetpensize node);

    void caseTTo(TTo node);

    void caseTMake(TMake node);

    void caseTRun(TRun node);

    void caseTRepeat(TRepeat node);

    void caseTIf(TIf node);

    void caseTIfelse(TIfelse node);

    void caseTStop(TStop node);

    void caseTGoto(TGoto node);

    void caseTEnd(TEnd node);

    void caseTTrue(TTrue node);

    void caseTFalse(TFalse node);

    void caseTLPar(TLPar node);

    void caseTRPar(TRPar node);

    void caseTLBrk(TLBrk node);

    void caseTRBrk(TRBrk node);

    void caseTLKey(TLKey node);

    void caseTRKey(TRKey node);

    void caseTPlus(TPlus node);

    void caseTMin(TMin node);

    void caseTDiv(TDiv node);

    void caseTMult(TMult node);

    void caseTSemi(TSemi node);

    void caseTComma(TComma node);

    void caseTIdentifier(TIdentifier node);

    void caseTInteger(TInteger node);

    void caseTDouble(TDouble node);

    void caseTEndline(TEndline node);

    void caseTComment(TComment node);

    void caseTBlank(TBlank node);

    void caseEOF(EOF node);
}
