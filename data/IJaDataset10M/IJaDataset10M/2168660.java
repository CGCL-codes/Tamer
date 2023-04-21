package org.orbeon.oxf.processor.pipeline.ast;

public interface ASTHandler {

    public boolean startPipeline(ASTPipeline pipeline);

    public void endPipeline(ASTPipeline pipeline);

    public void param(ASTParam param);

    public boolean startProcessorCall(ASTProcessorCall processorCall);

    public void endProcessorCall(ASTProcessorCall processorCall);

    public boolean startInput(ASTInput input);

    public void endInput(ASTInput input);

    public void output(ASTOutput output);

    public boolean startHrefAggregate(ASTHrefAggregate hrefAggregate);

    public void endHrefAggregate(ASTHrefAggregate hrefAggregate);

    public void hrefId(ASTHrefId hrefId);

    public void hrefURL(ASTHrefURL hrefURL);

    public boolean startHrefXPointer(ASTHrefXPointer hrefXPointer);

    public void endHrefXPointer(ASTHrefXPointer hrefXPointer);

    public boolean startChoose(ASTChoose choose);

    public void endChoose(ASTChoose choose);

    public boolean startForEach(ASTForEach forEach);

    public void endStartForEach(ASTForEach forEach);

    public void endForEach(ASTForEach forEach);

    public boolean startWhen(ASTWhen when);

    public void endWhen(ASTWhen when);
}
