package com.turnengine.client.global.translation.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;
import com.javabi.common.locale.LanguageCountry;
import com.turnengine.client.global.translation.bean.ITranslation;

/**
 * The Get Translation By Id Service Async.
 */
@GwtCompatible
public interface GetTranslationByIdServiceAsync extends IGeneratedRemoteServiceAsync {

    void getTranslationById(int id, LanguageCountry language, AsyncCallback<ITranslation> calback);
}
