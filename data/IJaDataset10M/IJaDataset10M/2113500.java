package ua.orion.core.persistence;

/**
 * Интерфейс вводит поле для ручного упорядочивания (сортировки) объектов. 
 * @author sl
 */
public interface IRangable {

    Integer getRang();

    void setRang(Integer rang);
}
