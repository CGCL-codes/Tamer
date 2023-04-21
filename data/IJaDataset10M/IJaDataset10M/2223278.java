package ru.susu.algebra.operation;

/**
 * Фасад для работы с операциями.
 *
 * @author akargapolov
 * @since: 22.04.2010
 */
public class OperationFacade {

    /**
	 * Безопасное извлечение параметра из списка.
	 *
	 * @param index индекс требуемого параметра
	 * @param defValue значение по-умолчанию
	 * @param sources исходные данные
	 * @return значение параметра
	 */
    public static Object getSourceSafe(int index, Object defValue, Object[] sources) {
        if (sources.length <= index) {
            return defValue;
        }
        return sources[index];
    }
}
