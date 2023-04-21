package tasksys;

import java.util.Iterator;

/**
 * 列挙用クラス
 * 実装はKeyEnum、PrioEnumによって行っています
 * @author zukkun
 *
 */
public abstract class Enumeration {

    protected Iterator<TaskControlBlock> listIt;

    public void setIterator(Iterator<TaskControlBlock> it) {
        listIt = it;
    }

    /**
	 * 次のオブジェクトを得る
	 * 任意の実装を期待するメソッドです
	 * @return GameObject
	 */
    public abstract GameObject getNext();
}
