package semestralka.client;

import java.util.List;
import semestralka.domain.Word;
import semestralka.shared.Languages;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("Translator")
public interface TranslatorService extends RemoteService {

    String greetServer(String name) throws IllegalArgumentException;

    public List<Word> getWords(int dictionary);

    String getDictionaryJSON(String word, Languages from, Languages to);
}
