package com.microfly.core;

import com.microfly.event.*;

/**
 * �¼�ִ�з���
 * a new publishing system
 * Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 */
public interface IEventAction {

    public void Insert(Object observer, InsertEvent event);

    public void Update(Object observer, UpdateEvent event);

    public void Delete(Object observer, DeleteEvent event);

    public void Ready(Object observer, Ready2PublishEvent event);

    public void Publish(Object observer, PublishEvent event);

    public void Cancel(Object observer, CancelEvent event);
}
