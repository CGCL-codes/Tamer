package com.turnengine.client.global.translation.command.set;

import static com.javabi.common.dependency.DependencyFactory.getDependency;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.errorcode.ErrorCodeException;
import com.javabi.command.executor.ICommandExecutorService;
import com.javabi.common.locale.LanguageCountry;
import com.turnengine.client.global.translation.bean.ITranslation;
import com.turnengine.client.global.translation.command.GetTranslationById;
import com.turnengine.client.global.translation.command.GetTranslationByText;
import com.turnengine.client.global.translation.command.IGetTranslationById;
import com.turnengine.client.global.translation.command.IGetTranslationByText;
import com.turnengine.client.global.translation.command.INewTranslation;
import com.turnengine.client.global.translation.command.ISetTranslation;
import com.turnengine.client.global.translation.command.NewTranslation;
import com.turnengine.client.global.translation.command.SetTranslation;

/**
 * The Global Translation Command Set.
 */
public class GlobalTranslationCommandSet implements IGlobalTranslationCommandSet {

    @Override
    public int newTranslation(long loginId, String english) {
        INewTranslation command = new NewTranslation(loginId, english);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<Integer> response = service.execute(command);
        if (response.hasErrors()) {
            throw new ErrorCodeException(response);
        }
        return response.getReturnValue();
    }

    @Override
    public int setTranslation(long loginId, int id, LanguageCountry language, String translation) {
        ISetTranslation command = new SetTranslation(loginId, id, language, translation);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<Integer> response = service.execute(command);
        if (response.hasErrors()) {
            throw new ErrorCodeException(response);
        }
        return response.getReturnValue();
    }

    @Override
    public ITranslation getTranslationById(int id, LanguageCountry language) {
        IGetTranslationById command = new GetTranslationById(id, language);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<ITranslation> response = service.execute(command);
        if (response.hasErrors()) {
            throw new ErrorCodeException(response);
        }
        return response.getReturnValue();
    }

    @Override
    public ITranslation getTranslationByText(String english, LanguageCountry language) {
        IGetTranslationByText command = new GetTranslationByText(english, language);
        ICommandExecutorService service = getDependency(ICommandExecutorService.class);
        IExecutableCommandResponse<ITranslation> response = service.execute(command);
        if (response.hasErrors()) {
            throw new ErrorCodeException(response);
        }
        return response.getReturnValue();
    }
}
