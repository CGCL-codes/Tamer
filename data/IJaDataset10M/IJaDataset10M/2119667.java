package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "uz" locale.
 */
public class DateTimeFormatInfoImpl_uz extends DateTimeFormatInfoImpl {

    @Override
    public String dateFormatShort() {
        return "yy/MM/dd";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "BCE", "CE" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "BCE", "CE" };
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "Январ", "Феврал", "Март", "Апрел", "Май", "Июн", "Июл", "Август", "Сентябр", "Октябр", "Ноябр", "Декабр" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "Я", "Ф", "М", "А", "М", "И", "И", "А", "С", "О", "Н", "Д" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "Q1", "Q2", "Q3", "Q4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "якшанба", "душанба", "сешанба", "чоршанба", "пайшанба", "жума", "шанба" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "Я", "Д", "С", "Ч", "П", "Ж", "Ш" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "Якш", "Душ", "Сеш", "Чор", "Пай", "Жум", "Шан" };
    }
}
