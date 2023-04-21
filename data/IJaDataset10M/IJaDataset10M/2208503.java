package ru.ipo.dces.buildutils.raw;

/**
 * Запрос: Получить список пользователей
 */
public class GetUsersRequest implements Request {

    /**
   * Идентификатор сессии. Допускаютс идентификаторы только администраторов соревнования или сервера
   */
    @BinInfo(phpDefaultValue = "null")
    public String sessionID;

    /**
   * Номер соревнования, по которому требуется получить список пользователей. Актуален для администратора
   * сервера. Администратор соревнования указывает -1 или id своего соревнования
   */
    @BinInfo(phpDefaultValue = "null")
    public int contestID;
}
