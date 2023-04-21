package com.vmware.spring.workshop.services.convert.impl;

import org.junit.Test;
import org.mockito.Mockito;
import com.vmware.spring.workshop.dao.api.BankDao;
import com.vmware.spring.workshop.dto.banking.BranchDTO;
import com.vmware.spring.workshop.model.banking.Bank;
import com.vmware.spring.workshop.model.banking.Branch;

/**
 * @author lgoldstein
 */
public class BranchDTOConverterTest extends AbstractDTOConverterTestSupport<Branch, BranchDTO> {

    public BranchDTOConverterTest() {
        super();
    }

    @Test
    public void testModel2DTOConversion() {
        final Branch data = createMockBranch();
        final BranchDTOConverterImpl converter = getConverter(data);
        final BranchDTO dto = checkModel2DTOConversion(data, converter);
        final Bank bank = data.getBank();
        final Long dataId = bank.getId(), dtoId = dto.getBankId();
        assertEquals("Mismatched bank ID(s)", dataId, dtoId);
    }

    @Test
    public void testDto2ModelConversion() {
        final BranchDTO dto = createMockBranchDTO();
        final BranchDTOConverterImpl converter = getConverter(dto);
        final Branch data = checkDTO2ModelConversion(dto, converter);
        final Bank bank = data.getBank();
        final Long dataId = bank.getId(), dtoId = dto.getBankId();
        assertEquals("Mismatched bank ID(s)", dataId, dtoId);
    }

    private BranchDTOConverterImpl getConverter(final Branch branch) {
        return getConverter(branch.getBank());
    }

    private BranchDTOConverterImpl getConverter(final BranchDTO branch) {
        return getConverter(branch.getBankId());
    }

    private BranchDTOConverterImpl getConverter(final Long bankId) {
        final Bank bank = BankDTOConverterTest.createMockBank();
        bank.setId(bankId);
        return getConverter(bank);
    }

    private BranchDTOConverterImpl getConverter(final Bank bank) {
        final BankDao daoBank = Mockito.mock(BankDao.class);
        final Long bankId = bank.getId();
        Mockito.when(daoBank.findOne(bankId)).thenReturn(bank);
        return new BranchDTOConverterImpl(daoBank);
    }

    static final Branch createMockBranch() {
        final long nanoTime = System.nanoTime(), msecTime = System.currentTimeMillis();
        final Branch data = new Branch();
        data.setId(Long.valueOf(nanoTime));
        data.setVersion((int) nanoTime);
        data.setBranchCode((int) (nanoTime ^ msecTime));
        data.setName("branch#" + data.getBranchCode());
        final Bank bank = BankDTOConverterTest.createMockBank();
        data.setBank(bank);
        data.setLocation(data.getName() + "@" + bank.getName());
        return data;
    }

    static final BranchDTO createMockBranchDTO() {
        final long nanoTime = System.nanoTime(), msecTime = System.currentTimeMillis();
        final BranchDTO dto = new BranchDTO();
        dto.setId(Long.valueOf(nanoTime));
        dto.setBranchCode((int) (nanoTime ^ msecTime));
        dto.setName("branch#" + dto.getBranchCode());
        dto.setBankId(Long.valueOf(msecTime ^ nanoTime));
        dto.setLocation(dto.getName() + "@" + dto.getBankId());
        return dto;
    }
}
