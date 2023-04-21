package com.brekeke.hiway.ticket.service;

import com.brekeke.hiway.ticket.dto.TicketDto;

/**
 * ［管理所、管理外、高管局］票据收发盘存明细账
 * @author LEPING.LI
 * @version 1.0.0
 */
public interface CommonTicketDetailService extends BaseService {

    /**
	 * 生成［管理所、管理外、高管局］票据收发盘存明细账
	 * @param ticketDto
	 */
    public void generateCommonTicketDetailLedger(TicketDto ticketDto);
}
