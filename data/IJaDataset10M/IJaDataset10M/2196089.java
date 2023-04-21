package ua.orion.cpu.core.persons.entities;

import java.util.Calendar;
import javax.persistence.*;
import ua.orion.cpu.core.entities.Document;

/**
 * Пасспорт. Содержит основную информацию о паспорте: серия, номер, кем выдан,
 * дата.
 *
 * @author molodec
 */
@Entity
public class Passport extends Document<Passport> {

    /**
     * Поле хранит информацию о том, кем был выдан паспорт.
     */
    private String whoGive;

    /**
     * Создание паспорта по умолчанию без указания информции документа.
     */
    public Passport() {
    }

    /**
     * Создание паспорта с указанием параметров
     *
     * @param serial - серия
     * @param number - номер
     * @param issue - дата выдачи
     * @param whoGive - кем выдан
     */
    public Passport(String serial, String number, Calendar issue, String whoGive) {
        setSerial(serial);
        setNumber(number);
        setIssue(issue);
        this.whoGive = whoGive;
    }

    /**
     * Получение информации о том, кто выдал паспорт
     *
     * @return строка с информацией о том, кто выдал паспорт
     */
    public String getWhoGive() {
        return whoGive;
    }

    /**
     * Установка графы "кем выдан"
     *
     * @param whoGive - кем выдан
     */
    public void setWhoGive(String whoGive) {
        this.whoGive = whoGive;
    }
}
