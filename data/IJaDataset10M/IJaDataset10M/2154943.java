package org.seasar.webhelpers.validator.reader;

import java.util.Map;
import org.seasar.webhelpers.validator.exception.ValidatorException;

/**
 * {@link ValidationDataTable}から{@link ValidationContext]を生成します。
 * 
 * @author takanori
 * 
 */
public interface ValidationContextProvider {

    /**
     * バリデーション定義を提供します。<br>
     * 
     * @return バリデーション定義のマップ
     * @throws バリデーション定義の生成に失敗した場合
     */
    Map provide(ValidationDataTable dataTable) throws ValidatorException;
}
