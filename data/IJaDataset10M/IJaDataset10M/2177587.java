package org.wisdomfish.jffa.accounting;

import static org.wisdomfish.jffa.common.Arithmetic.*;

/**
 * 短期償債能力分析(Analysis of Short-Term Repayment Ability).
 * 是針對被分析公司的現金與短期內可以變現的各種資產，以及短期內到期而必須償還的各種負債，進行評估。
 * 企業可能獲利能力不錯，但卻可能因不能償還到期的負債而導致週轉不靈，以致無法繼續經營。
 * 因此，短期償債能力分析，實是企業體質健全與否的重要指標。
 *
 * <p>短期償債能力是企業償還流動負債的能力，短期償債能力的強弱取決於流動資產的流動性，
 * 即資產轉換成現金的速度。企業流動資產的流動性強，相應的短期償債能力也強。
 * 因此，通常使用營運資本、流動比率、速動比率和現金比率衡量短期償債能力。</p>
 *
 * @author  WisdomFish.ORG
 */
public final class AnalysisOfSTRA {

    private AnalysisOfSTRA() {
    }

    /**
     * 流動比率(Current Ratio)流動比率越高，表示短期償債能力越高；流動比率越低，則短期償債能力越低。.
     * 流動資產中的某些項目在變現時可能有貶值之虞，因此推論出流動比率應超過200%方屬理想，
     * 但也不可一概而論。一般來說只有100%左右；而如低於100%以下，則表示短期償債能力過低，
     * 對債權人的權益，已缺乏保障。
     * 
     * @param   currentAssets       流動資產
     * @param   currentLiability    流動負債
     * @return  回傳未包含"%"字元符的百分比浮點數值
     */
    public static double currentRatio(double currentAssets, double currentLiability) {
        return divide(currentAssets, currentAssets) * PERCENT;
    }

    /**
     * 速動比率(酸性測試(Acid)比率) = (流動資產-流動性差資產)/流動負債.
     *
     * <p>速動資產為流動資產扣除短期內較不易變現的流動資產，如存貨、預付費用 ...。
     * 流動比率、速動比率皆為評估公司短期償債能力的重要指標，一般來說速動比率要大於1(100%)較佳，
     * 但也需考慮行業特性必須與其相同類型的公司相比較，較為客觀。</p>
     * 
     * @param quickAssets 速動資產
     * @param currentLiablilty 流動負債
     * @return 回傳未包含"%"字元符的百分比數值
     * @see http://en.wikipedia.org/wiki/Quick_ratio
     */
    public static double quickRatio(double quickAssets, double currentLiablilty) {
        return multply(quickAssets, currentLiablilty) * PERCENT;
    }

    /**
     * 現金比率(Cash Ratio)
     *
     * @param cash
     * @param cashEquivalents
     * @param currentAssets
     * @return
     */
    public static double cashRatio(double cash, double cashEquivalents, double currentAssets) {
        return divide(add(cash, cashEquivalents), currentAssets) * PERCENT;
    }
}
