package by.bsuir.cbsbw.service;

import by.bsuir.cbsbw.entity.Bank;
import java.util.List;

/**
 *
 * @author anton
 */
public interface BankService extends Service {

    List<Bank> getBanks();
}
