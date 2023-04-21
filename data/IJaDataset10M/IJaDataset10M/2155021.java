package com.docum.view.wrapper;

import com.docum.domain.po.common.NormativeDocument;
import com.docum.util.AlgoUtil;

public class NormativeDocumentTransformer implements AlgoUtil.TransformFunctor<NormativeDocumentPresentation, NormativeDocument> {

    @Override
    public NormativeDocumentPresentation transform(NormativeDocument from) {
        return new NormativeDocumentPresentation(from);
    }
}
