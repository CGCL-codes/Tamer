package pl.xperios.rdk.client.dictionaries;

import pl.xperios.rdk.client.common.DictionaryChooserImpl;
import pl.xperios.rdk.client.common.DictionaryableGet;
import pl.xperios.rdk.shared.dtos.Roles4UsersActual;

public class Roles4UsersActualDictionaryChooser extends DictionaryChooserImpl<Roles4UsersActual, Long> {

    @Override
    public DictionaryableGet<Roles4UsersActual> getDictionary() {
        return new Roles4UsersActualDictionary();
    }
}
