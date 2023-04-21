package ru.adv.repository.channel.io;

import java.io.Serializable;

public class SQLRC implements RemoteCommand, Serializable {

    public String sql;
}
