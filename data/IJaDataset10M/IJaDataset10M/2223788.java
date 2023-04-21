package org.gocha.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gocha.collection.iterators.AddIterable;
import org.gocha.collection.iterators.ArrayIterable;
import org.gocha.collection.iterators.BufferIterable;
import org.gocha.collection.iterators.ConvertIterable;
import org.gocha.collection.iterators.EmptyIterable;
import org.gocha.collection.iterators.CompareEqu;
import org.gocha.collection.iterators.MinMaxIterable;
import org.gocha.collection.iterators.PredicateIterable;
import org.gocha.collection.iterators.ReverseInterable;
import org.gocha.collection.iterators.SetIterable;
import org.gocha.collection.iterators.SingleIterable;
import org.gocha.collection.iterators.SubIterable;
import org.gocha.collection.iterators.TreeIterable;
import org.gocha.collection.iterators.XMLNodeIterable;

/**
 * Класс по работе с итераторами
 * @author gocha
 */
public class Iterators {

    /**
     * Конвертирует последовательность в список
     * @param <T> Тип объектов в последовательности
     * @param src Исходная последовательность
     * @param listClass Класс реализующий список (должен иметь конструктор по умолчанию)
     * @return Список или null если не смог создать список
     */
    public static <T> List<T> toList(Iterable<? extends T> src, Class<? extends List> listClass) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (listClass == null) {
            throw new IllegalArgumentException("listClass == null");
        }
        try {
            List result = listClass.newInstance();
            addTo(src, result);
            return result;
        } catch (InstantiationException ex) {
            Logger.getLogger(Iterators.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Iterators.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Конвертирует последовательность в массив
     * @param <T> Тип объектов в последовательности
     * @param src Исходная последовательность
     * @param array Пустой массив
     * @return Сконвертированная последовательность
     */
    public static <T> T[] toArray(Iterable<? extends T> src, T[] array) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (array == null) {
            throw new IllegalArgumentException("array == null");
        }
        return toArrayList(src).toArray(array);
    }

    /**
     * Конвертирует последовательность в список
     * @param <T> Тип объектов в последовательности
     * @param src Исходная последовательность
     * @return Список
     */
    public static <T> ArrayList<T> toArrayList(Iterable<? extends T> src) {
        return (ArrayList<T>) toList(src, ArrayList.class);
    }

    /**
     * Конвертирует последовательность в список
     * @param <T> Тип объектов в последовательности
     * @param src Исходная последовательность
     * @return Список
     */
    public static <T> Vector<T> toVector(Iterable<? extends T> src) {
        return (Vector<T>) toList(src, Vector.class);
    }

    /**
     * Итератор - Фильт возвращающий объекты заданого класса (сравнивае строго)
     * @param <T> Интересующий класс
     * @param src Исходное множество объектов
     * @param c Интересующий класс
     * @param includeNull Включать или нет пустые ссылки
     * @return Последовательность объектов определенного класса
     */
    public static <T> Iterable<T> classFilter(Iterable src, Class<T> c, boolean includeNull) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (c == null) {
            throw new IllegalArgumentException("c == null");
        }
        final boolean incNull = includeNull;
        final Class need = c;
        Predicate<T> p = new Predicate<T>() {

            public boolean validate(T value) {
                if (value == null && incNull) return true;
                Class c = value.getClass();
                return need.equals(c);
            }
        };
        return predicate(src, p);
    }

    /**
     * Итератор - Фильт возвращающий объекты заданого класса.
     * <p>
     * Сравнение объектов производиться функцией isAssignableFrom т.е.
     * <b><i>объект</i> instanceof <i>Интересующий класс</i></b>
     * </p>
     * @param <T> Интересующий класс
     * @param src Исходное множество объектов
     * @param c Интересующий класс
     * @param includeNull Включать или нет пустые ссылки
     * @return Последовательность объектов определенного класса
     */
    public static <T> Iterable<T> isClassFilter(Iterable src, Class<T> c, boolean includeNull) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (c == null) {
            throw new IllegalArgumentException("c == null");
        }
        final boolean incNull = includeNull;
        final Class need = c;
        Predicate<T> p = new Predicate<T>() {

            public boolean validate(T value) {
                if (value == null && incNull) return true;
                Class c = value.getClass();
                return need.isAssignableFrom(c);
            }
        };
        return predicate(src, p);
    }

    /**
     * Итератор фильтр - не возвращает пустые ссылки
     * @param <T> Тип объектов
     * @param src Исходная последовательность
     * @return Ссылки на объекты
     */
    public static <T> Iterable<T> notNullFilter(Iterable<T> src) {
        return predicate(src, Predicates.<T>isNotNull());
    }

    /**
     * Конвертирует массив в итератор
     * @param <T> Тип объектов
     * @param array Массив
     * @return Последовательность
     */
    public static <T> Iterable<T> array(T[] array) {
        return new ArrayIterable<T>(array);
    }

    /**
     * Проверяет находится ли объект в массиве
     * @param v Объект
     * @param src Массив
     * @return true - находиться, false - не находится
     */
    public static boolean in(Object v, Object[] src) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        return in(v, array(src));
    }

    /**
     * Проверяет находится ли объект в последовательности
     * @param v Объект
     * @param src Последовательность
     * @return true - находиться, false - не находится
     */
    public static boolean in(Object v, Iterable src) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (v == null) {
            for (Object a : src) {
                if (a == null) return true;
            }
            return false;
        } else {
            for (Object b : src) {
                if (v.equals(b)) return true;
            }
            return false;
        }
    }

    /**
     * Подсчитывает кол-во элементов в последовательности
     * @param <T> Тип объектов
     * @param src Исходная последовательность
     * @return Кол-во элементов
     */
    public static <T> long count(Iterable<T> src) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        long co = 0;
        for (@SuppressWarnings("unused") T t : src) {
            co++;
        }
        return co;
    }

    /**
     * Добавляет объекты из последовательности в коллекцию
     * @param <T> Тип объектов
     * @param src Исходная последовательность
     * @param collection Коллекция
     */
    public static <T> void addTo(Iterable<T> src, Collection<T> collection) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (collection == null) {
            throw new IllegalArgumentException("collection == null");
        }
        for (T o : src) collection.add(o);
    }

    /**
     * Возвращает пустую последовательность объектов
     * @param <T> Тип значений в последовательностях
     * @return Пустая последовательность
     */
    public static <T> Iterable<T> empty() {
        return new EmptyIterable<T>();
    }

    /**
     * Возвращает последовательность с одним элементом
     * @param <T> Тип значений в последовательностях
     * @param item Элемент последовательности
     * @return Последовательность
     */
    public static <T> Iterable<T> single(T item) {
        return new SingleIterable<T>(item);
    }

    /**
     * Итератор объеденяющий последовательность значений заданых другими итераторами.
     * <p>
     * Для примера:<br/>
     * Первая последовательность объектов: <b>{ A, B, C }</b> <br/>
     * Вторая последовательность объектов: <b>{ D, E, C }</b> <br/>
     * Результирующая последовательность будет: <b>{ A, B, C, D, E, C }</b>
     * </p>
     * @param <T> Тип данных в итераторе
     * @param src Исходные итераторы
     * @return Результирующий итератор
     */
    public static <T> Iterable<T> add(Iterable<T>... src) {
        return new AddIterable<T>(src);
    }

    /**
     * Итератор объеденяющий последовательность значений заданых другими итераторами.
     * <p>
     * Для примера:<br/>
     * Первая последовательность объектов: <b>{ A, B, C }</b> <br/>
     * Вторая последовательность объектов: <b>{ D, E, C }</b> <br/>
     * Результирующая последовательность будет: <b>{ A, B, C, D, E, C }</b>
     * </p>
     * @param <T> Тип данных в итераторе
     * @param src Исходные итераторы
     * @return Результирующий итератор
     */
    public static <T> Iterable<T> sequence(Iterable<T>... src) {
        return new AddIterable<T>(src);
    }

    /**
     * Вычитает из исходной последовательности объекты заданые второй последовательностью
     * <p>
     * Для примера:<br/>
     * Первая последовательность объектов: <b>{ A, B, C }</b> <br/>
     * Вторая последовательность объектов: <b>{ D, E, C }</b> <br/>
     * Результирующая последовательность будет: <b>{ A, B }</b>
     * </p>
     * <p>
     * В качестве сравнения объектов на равенство будет использоватся метод equals
     * </p>
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param sub Вычитаемая последовательность
     * @return Результатируемая последовательность
     */
    public static <T> Iterable<T> sub(Iterable<T> src, Iterable<T> sub) {
        return new SubIterable<T>(src, sub);
    }

    /**
     * Вычитает из исходной последовательности объекты заданые второй последовательностью
     * <p>
     * Для примера:<br/>
     * Первая последовательность объектов: <b>{ A, B, C }</b> <br/>
     * Вторая последовательность объектов: <b>{ D, E, C }</b> <br/>
     * Результирующая последовательность будет: <b>{ A, B }</b>
     * </p>
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param sub Вычитаемая последовательность
     * @param cmp Интерфес сравнения на равенство объектов
     * @return Результатируемая последовательность
     */
    public static <T> Iterable<T> sub(Iterable<T> src, Iterable<T> sub, CompareEqu<T> cmp) {
        return new SubIterable<T>(src, sub, cmp);
    }

    /**
     * Вычитает из исходной последовательности объекты заданые второй последовательностью
     * <p>
     * Для примера:<br/>
     * Первая последовательность объектов: <b>{ A, B, C }</b> <br/>
     * Вторая последовательность объектов: <b>{ D, E, C }</b> <br/>
     * Результирующая последовательность будет: <b>{ A, B }</b>
     * </p>
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param sub Вычитаемая последовательность
     * @param cmp Интерфес сравнения на равенство объектов
     * @return Результатируемая последовательность
     */
    public static <T> Iterable<T> sub(Iterable<T> src, Iterable<T> sub, Comparator<T> cmp) {
        final Comparator fcmp = cmp;
        return sub(src, sub, new CompareEqu() {

            public boolean isEqu(Object a, Object b) {
                if (fcmp == null) {
                    return a == null ? b == null : a.equals(b);
                } else {
                    return fcmp.compare(a, b) == 0;
                }
            }
        });
    }

    /**
     * Итератор использующий буффер объектов. Предварительно копирует объекты в буфер и уже по ним проходит.
     * <p>
     * В качестве буфера используется класс java.util.ArrayList
     * </p>
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @return Копия объектов
     */
    public static <T> Iterable<T> buffer(Iterable<T> src) {
        return new BufferIterable<T>(src);
    }

    /**
     * Итератор использующий буффер объектов. Предварительно копирует объекты в буфер и уже по ним проходит.
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param buffer Буфер объектов
     * @return Копия объектов
     */
    public static <T> Iterable<T> buffer(Iterable<T> src, List<T> buffer) {
        return new BufferIterable<T>(buffer, src);
    }

    /**
     * Возвращает последовательность содержащую только те объекты, которые удалетворяют предикату
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param predicate Предикат
     * @return Последовательность значений удалетворяющих предикату
     */
    public static <T> Iterable<T> predicate(Iterable<T> src, Predicate<T> predicate) {
        return new PredicateIterable<T>(predicate, src);
    }

    /**
     * Возвращает последовательность содержащую только те объекты, которые удалетворяют предикату
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param predicate Предикат
     * @return Последовательность значений удалетворяющих предикату
     */
    public static <T> Iterable<T> predicate(T[] src, Predicate<T> predicate) {
        return new PredicateIterable<T>(predicate, array(src));
    }

    /**
     * Возвращает последовательность содержащую объекты сконвертированные в другой тип данных
     * @param <From> Тип данных из которого необходимо сконвертировать
     * @param <To> Тип данных в который необходимо сконвертировать
     * @param src Исходная последовательность
     * @param convertor Конвертор типов
     * @return Последовательность сконвертированых объектов
     */
    public static <From, To> Iterable<To> convert(Iterable<From> src, Convertor<From, To> convertor) {
        return new ConvertIterable<From, To>(src, convertor);
    }

    /**
     * Возвращает последовательность содержащую объекты сконвертированные в другой тип данных
     * @param <From> Тип данных из которого необходимо сконвертировать
     * @param <To> Тип данных в который необходимо сконвертировать
     * @param src Исходная последовательность
     * @param convertor Конвертор типов
     * @return Последовательность сконвертированых объектов
     */
    public static <From, To> Iterable<To> convert(From[] src, Convertor<From, To> convertor) {
        return convert(array(src), convertor);
    }

    /**
     * Итератор возвращающий минимальные значения из указанй последовательности
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param cmp Интерфейс сравнения объектов
     * @return Последовательность содержащая минимальные значения
     */
    public static <T> Iterable<T> min(Iterable<T> src, Comparator<T> cmp) {
        return new MinMaxIterable<T>(src, cmp, false);
    }

    /**
     * Итератор возвращающий максимальные значения из указанй последовательности
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param cmp Интерфейс сравнения объектов
     * @return Последовательность содержащая максимальные значения
     */
    public static <T> Iterable<T> max(Iterable<T> src, Comparator<T> cmp) {
        return new MinMaxIterable<T>(src, cmp, true);
    }

    /**
     * Создает обратную последовательность значений
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @return Итератор содержащая обратную последовательность значения
     */
    public static <T> Iterable<T> reverse(Iterable<T> src) {
        return new ReverseInterable<T>(src);
    }

    /**
     * Создает последовательность неповторяющихся объектов
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @return Последовательнось неповторяющихся объектов
     */
    public static <T> Iterable<T> set(Iterable<T> src) {
        return new SetIterable<T>(src);
    }

    /**
     *
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param comparer Интерфейс сравнения объектов
     * @return Последовательнось неповторяющихся объектов
     */
    public static <T> Iterable<T> set(Iterable<T> src, CompareEqu<T> comparer) {
        return new SetIterable<T>(src, comparer);
    }

    /**
     * Итератор по деверу объектов заданному через интерфес NodesExtracter
     * @param <T> Тип значений в последовательностях
     * @param src Корневой объект
     * @param extracter Итерфес доступа к дочерним элементам
     * @return Последовательность объектов
     */
    public static <T> Iterable<T> tree(T src, org.gocha.collection.NodesExtracter<T, T> extracter) {
        return new TreeIterable<T>(src, extracter);
    }

    /**
     * Итератор по списку XML узлов
     * @param nl Список XML узлов
     * @return Последовательность XML Узлов
     */
    public static Iterable<org.w3c.dom.Node> xmlNodes(org.w3c.dom.NodeList nl) {
        return new XMLNodeIterable(nl);
    }

    /**
     * Возвращает отсортированную последовательность по определенному критерию
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @param comparer Критерий сортировки
     * @return Отсортированная последовательность
     */
    public static <T> Iterable<T> sort(Iterable<T> src, Comparator<T> comparer) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (comparer == null) {
            throw new IllegalArgumentException("comparer == null");
        }
        List<T> list = toList(src, ArrayList.class);
        Collections.sort(list, comparer);
        return list;
    }

    /**
     * Возвращает отсортированную последовательность
     * @param <T> Тип значений в последовательностях
     * @param src Исходная последовательность
     * @return Отсортированная последовательность
     */
    public static <T extends Comparable<? super T>> Iterable<T> sort(Iterable<T> src) {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        List<T> list = toList(src, ArrayList.class);
        Collections.sort(list);
        return list;
    }
}
