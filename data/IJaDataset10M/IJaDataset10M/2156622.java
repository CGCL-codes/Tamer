package ac.jp.u_tokyo.SyncLib.language;

import ac.jp.u_tokyo.SyncLib.language.factories.KeyDictMapCombinatorFactory;

public class KeyDictMapBody extends DictMapBody {

    public KeyDictMapBody(int line) {
        super(line);
    }

    @Override
    protected String getFactoryName() {
        return KeyDictMapCombinatorFactory.class.getName();
    }

    @Override
    public int getParaCount() throws EvaluationFailedException {
        return _inner.getParaCount() - _factoryList.size();
    }
}
