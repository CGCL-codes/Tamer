package org.t2framework.samples.spring.service.impl;

import org.springframework.stereotype.Service;
import org.t2framework.samples.spring.service.CalcService;

/**
 * CalcServiceImpl
 * 
 * @author yone098
 * 
 */
@Service("calcService")
public class CalcServiceImpl implements CalcService {

    @Override
    public int calclate(int arg1, int arg2) {
        return arg1 + arg2;
    }
}
