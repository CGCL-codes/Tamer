package bdc.values;

/**
 * @author Андрей
 * 
 */
public interface Values2d {

    /**
	 * Заносит значение в список.
	 * 
	 * @param i
	 *            номер строки
	 * @param j
	 *            номер столбца
	 * @param nValue
	 *            вставляемое значение
	 */
    public void set(int i, int j, double nValue);

    /**
	 * Возвращает значение [i,j]-элемента.
	 * 
	 * @param i
	 *            номер строки
	 * @param j
	 *            номер столбца
	 * @return возвращает значение [i,j]-элемента списка
	 */
    public double get(int i, int j);

    /**
	          *
	          */
    public void clean();
}
