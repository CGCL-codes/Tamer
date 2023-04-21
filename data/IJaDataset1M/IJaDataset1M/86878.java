package ru.ipo.dces.buildutils.raw;

/**
 * Ответ: список участников соревнования
 */
public class GetUsersResponse implements Response {

    /**
   * Массив с описаниями участников
   */
    public UserDescription[] users;
}
