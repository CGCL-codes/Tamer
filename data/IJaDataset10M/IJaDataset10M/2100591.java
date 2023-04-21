package net.sf.gham.core.entity.transfer;

import net.sf.gham.core.util.PriceCellRenderer;
import net.sf.gham.swing.cellrenderer.CenterCellRender;
import net.sf.gham.swing.table.column.ColumnSavable;
import net.sf.gham.swing.table.column.ColumnSavableRef;
import net.sf.gham.swing.treetable.model.groupby.aggregator.AverageIntAggregator;
import net.sf.gham.swing.treetable.model.groupby.aggregator.TotalAggregator;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 * 
 */
public interface TransferColumns {

    String CAT_SALE = Messages.getString("Sale");

    String CAT_PURCHASE = Messages.getString("Purchase");

    String CAT_GENERAL = Messages.getString("General");

    ColumnSavable<Transfer> PLAYERNAME = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Name"), "PLAYERNAME", "PlayerName").setColumnCategory(CAT_GENERAL).setUsableAsColumn(false);

    ColumnSavable<Transfer> PROFIT = new ColumnSavableRef<Transfer>(Transfer.class, "Profit", PriceCellRenderer.singleton()).setAggregator(TotalAggregator.singleton()).setColumnCategory(CAT_GENERAL);

    ColumnSavable<Transfer> DATEOFPURCHASE = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("PurchaseDate"), "DATEOFPURCHASE", "DateOfPurchaseHattrickWeek", CenterCellRender.singleton()).setColumnCategory(CAT_PURCHASE);

    ColumnSavable<Transfer> DATEOFSALE = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("SaleDate"), "DATEOFSALE", "DateOfSaleHattrickWeek", CenterCellRender.singleton()).setColumnCategory(CAT_SALE);

    ColumnSavable<Transfer> PURCHASESEASON = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Purchase_season"), "PURCHASESEASON", "PurchaseSeason", CenterCellRender.singleton()).setGroupable(true).setColumnCategory(CAT_PURCHASE).setUsableAsColumn(false);

    ColumnSavable<Transfer> PURCHASEWEEK = new ColumnSavableRef<Transfer>(Transfer.class, "PurchaseWeek", CenterCellRender.singleton()).setGroupable(true).setColumnCategory(CAT_PURCHASE).setUsableAsColumn(false);

    ColumnSavable<Transfer> PURCHASEDAY = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Purchase_day"), "PURCHASEDAY", "PurchaseDay", CenterCellRender.singleton()).setColumnCategory(CAT_PURCHASE);

    ColumnSavable<Transfer> SALESEASON = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Sale_season"), "SALESEASON", "SaleSeason", CenterCellRender.singleton()).setGroupable(true).setColumnCategory(CAT_SALE).setUsableAsColumn(false);

    ColumnSavable<Transfer> SALEWEEK = new ColumnSavableRef<Transfer>(Transfer.class, "SaleWeek", CenterCellRender.singleton()).setGroupable(true).setColumnCategory(CAT_SALE).setUsableAsColumn(false);

    ColumnSavable<Transfer> SALEDAY = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Sale_day"), "SALEDAY", "SaleDay", CenterCellRender.singleton()).setColumnCategory(CAT_SALE);

    ColumnSavable<Transfer> SELLERTEAMNAME = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Seller"), "SELLERTEAMNAME", "SellerTeamName").setColumnCategory(CAT_PURCHASE);

    ColumnSavable<Transfer> BUYERTEAMNAME = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Buyer"), "BUYERTEAMNAME", "BuyerTeamName").setColumnCategory(CAT_SALE);

    ColumnSavable<Transfer> PURCHASEPRICE = new ColumnSavableRef<Transfer>(Transfer.class, "PurchasePrice", PriceCellRenderer.singleton()).setAggregator(TotalAggregator.singleton()).setColumnCategory(CAT_PURCHASE);

    ColumnSavable<Transfer> SALEPRICE = new ColumnSavableRef<Transfer>(Transfer.class, "SalePrice", PriceCellRenderer.singleton()).setAggregator(TotalAggregator.singleton()).setColumnCategory(CAT_SALE);

    ColumnSavable<Transfer> PURCHASEPRICEDIV = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("PurchasePrice"), "PURCHASEPRICEDIV", "PurchasePriceDiv").setGroupable(true).setColumnCategory(CAT_PURCHASE).setUsableAsColumn(false);

    ColumnSavable<Transfer> SALEPRICEDIV = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("SalePrice"), "SALEPRICEDIV", "SalePriceDiv").setGroupable(true).setColumnCategory(CAT_SALE).setUsableAsColumn(false);

    ColumnSavable<Transfer> DAYSINTEAM = new ColumnSavableRef<Transfer>(Transfer.class, Messages.getString("Days_in_team"), "DAYSINTEAM", "DaysInTeam", CenterCellRender.singleton()).setColumnCategory(CAT_GENERAL);

    ColumnSavable<Transfer> PROFITDIVDAYS = new ColumnSavableRef<Transfer>(Transfer.class, "ProfitDivDays", PriceCellRenderer.singleton()).setColumnCategory(CAT_GENERAL);

    ColumnSavable<Transfer> PURCHASETSI = new ColumnSavableRef<Transfer>(Transfer.class, "PurchaseTsi", CenterCellRender.singleton()).setAggregator(AverageIntAggregator.singleton()).setColumnCategory(CAT_PURCHASE);

    ColumnSavable<Transfer> SALETSI = new ColumnSavableRef<Transfer>(Transfer.class, "SaleTsi", CenterCellRender.singleton()).setAggregator(AverageIntAggregator.singleton()).setColumnCategory(CAT_SALE);
}
