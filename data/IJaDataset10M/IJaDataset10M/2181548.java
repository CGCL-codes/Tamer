package com.justin.foundation.ejb;

import com.justin.foundation.dto.IDto;
import com.justin.foundation.event.IEvent;

public interface IServiceInvokerCallback {

    public Object callback(Class<? extends IEvent> clazz, IDto dto);
}
