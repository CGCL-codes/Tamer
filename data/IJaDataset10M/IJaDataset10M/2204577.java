package com.anasoft.os.bookworm.service;

import java.io.Serializable;

public interface Request<RS extends Response> extends Serializable {

    RS process();
}
