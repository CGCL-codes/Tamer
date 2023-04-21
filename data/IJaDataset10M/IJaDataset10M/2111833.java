package ui.controller.manager;

import message.MessageId;
import ui.controller.initializer.search.ExpensesArticlesDialogInitializer;

public class ExpenseArticleManager extends StandardUIModelManager {

    public ExpenseArticleManager() {
        super(new ExpensesArticlesDialogInitializer(), store().expensesArticles(), MessageId.expenseArticle);
    }
}
